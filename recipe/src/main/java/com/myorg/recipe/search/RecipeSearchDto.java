package com.myorg.recipe.search;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSearchDto {
    private List<SearchCriteria> searchCriteriaList;
    @Nullable
    private String dataOption;

}