package org.palpalmans.ollive_back.domain.recipe.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Document(collection = "recipes")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Recipe {
    @Id
    private String id;

    private String amount;
    private List<Category> categories;
    private String difficulty;

    @Field("id")
    private Long recipeId;

    private List<Ingredient> ingredients;
    private List<ProcessStep> process;
    private Double score;
    private String thumbnailUrl;
    private String time;
    private String title;

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class Category {
        private String name;
        private int category_id;
        private int recipe_case_id;
        private String recipe_case_name;
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class Ingredient {
        private String name;
        private String amount;
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class ProcessStep {
        private String content;
        private String imageUrl;
        private int cookOrder;
    }
}