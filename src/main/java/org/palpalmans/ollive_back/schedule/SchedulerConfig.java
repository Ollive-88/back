package org.palpalmans.ollive_back.schedule;

import lombok.RequiredArgsConstructor;
import org.palpalmans.ollive_back.batch.RecipeScoreProcessingConfiguration;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {
    private final JobLauncher jobLauncher;
    private final RecipeScoreProcessingConfiguration job;
    private final JobRepository jobRepository;

    @Scheduled(cron = "0 0 2 * * ?") // 매일 새벽 2시에 실행
    public void runBatchJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("레시피 평점 갱신", "exchangeJob" + System.currentTimeMillis())
                .toJobParameters();

        try {
            JobExecution jobExecution = jobLauncher.run(job.updateRecipeScoreJob(jobRepository), jobParameters);
            System.out.println("Job Status : " + jobExecution.getStatus());
        } catch (JobExecutionException e) {
            System.out.println("Job Execution failed");
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}