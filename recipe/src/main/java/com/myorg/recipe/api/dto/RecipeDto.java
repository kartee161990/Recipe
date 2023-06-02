package com.myorg.recipe.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myorg.recipe.entity.Ingredient;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecipeDto {
    @JsonIgnore
    private Long id;
    private String name;
    private String instructions;
    private Boolean isVegetarian ;
    private Integer servingSize;
    private List<Ingredient> ingredient;
}
