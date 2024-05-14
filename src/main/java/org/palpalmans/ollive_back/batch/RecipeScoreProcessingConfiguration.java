package org.palpalmans.ollive_back.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.palpalmans.ollive_back.domain.recipe.model.entity.Recipe;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RecipeScoreProcessingConfiguration {
    private final DataSource dataSource;
    private final MongoTemplate mongoTemplate;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public JdbcPagingItemReader<RecipeScoreCalculation> reader(DataSource dataSource) throws Exception {
        JdbcPagingItemReader<RecipeScoreCalculation> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setPageSize(1000);

        // 쿼리 프로바이더 설정
        reader.setQueryProvider(createQueryProvider());

        // RowMapper 설정
        reader.setRowMapper(new RecipeScoreCalculationRowMapper());

        return reader;
    }

    // 쿼리 프로바이더 생성을 위한 메소드
    private PagingQueryProvider createQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource); // DataSource 설정
        queryProvider.setSelectClause("SELECT recipe_id, AVG(score) as avg_score"); // SELECT 절
        queryProvider.setFromClause("FROM recipe_score"); // FROM 절
        queryProvider.setGroupClause("GROUP BY recipe_id"); // GROUP BY 절

        // 정렬 키 설정 - 페이징을 위해 필수적임
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("recipe_id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        return queryProvider.getObject();
    }

    @Bean
    public ItemWriter<RecipeScoreCalculation> writer(MongoTemplate mongoTemplate) {
        return items -> {
            BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Recipe.class);
            for (RecipeScoreCalculation score : items) {
                Query query = new Query(Criteria.where("recipeId").is(score.getRecipeId()));
                Update update = new Update().set("score", score.getAvgScore());
                bulkOps.updateOne(query, update);
            }
            bulkOps.execute();
        };
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("step1", jobRepository)
                .<RecipeScoreCalculation, RecipeScoreCalculation>chunk(1000, transactionManager)
                .reader(reader(dataSource))
                .writer(writer(mongoTemplate))
                .build();
    }

    @Bean
    public Job updateRecipeScoreJob(JobRepository jobRepository) throws Exception {
        return new JobBuilder("updateRecipeScoreJob",jobRepository)
                .start(step1(jobRepository, platformTransactionManager))
                .build();
    }

}
