package org.palpalmans.ollive_back.batch;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipeScoreCalculationRowMapper implements RowMapper<RecipeScoreCalculation> {
    @Override
    public RecipeScoreCalculation mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new RecipeScoreCalculation(
                rs.getLong("recipe_id"),
                rs.getDouble("avg_score")
        );
    }
}