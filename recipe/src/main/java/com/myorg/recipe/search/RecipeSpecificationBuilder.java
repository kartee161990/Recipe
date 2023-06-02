package com.myorg.recipe.search;

import com.myorg.recipe.entity.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RecipeSpecificationBuilder {

    private final List<SearchCriteria> criteriaList;
    private String dataOption;

    public RecipeSpecificationBuilder(){
        this.criteriaList = new ArrayList<>();
        this.dataOption = "all";
    }


    public final RecipeSpecificationBuilder with(SearchCriteria searchCriteria){
        criteriaList.add(searchCriteria);
        return this;
    }

    public final RecipeSpecificationBuilder with(String s) {
        dataOption = s;
        return this;
    }

    public Specification<Recipe> build(){
        if (criteriaList.size() == 0) {
            return null;
        }

        Specification<Recipe> recipeSpecification = new RecipeSpecification(criteriaList.get(0));
        SearchOperation searchOperation = SearchOperation.getDataOption(dataOption);

        if (searchOperation == null) {
            log.error("Unsupported data option : {}", dataOption);
            throw new UnsupportedOperationException("Unsupported data option : " + dataOption);
        }

        for (int i = 1; i < criteriaList.size(); i++) {
            SearchCriteria criteria = criteriaList.get(i);

            recipeSpecification = (searchOperation == SearchOperation.ALL)
                    ? Specification.where(recipeSpecification).and(new RecipeSpecification(criteria))
                    : Specification.where(recipeSpecification).or(new RecipeSpecification(criteria));
        }

        return recipeSpecification;
    }
}
