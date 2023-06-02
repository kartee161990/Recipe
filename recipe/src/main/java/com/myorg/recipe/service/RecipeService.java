package com.myorg.recipe.service;

import com.myorg.recipe.entity.Recipe;
import com.myorg.recipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Optional<Recipe> findByRecipeId(Long recipeId){
        return recipeRepository.findById(recipeId);
    }

    public Recipe updateRecipe(Recipe foundRecipe, Recipe recipeInp){
        foundRecipe.setIsVegetarian(recipeInp.getIsVegetarian());
        foundRecipe.setInstructions(recipeInp.getInstructions());
        foundRecipe.setServingSize(recipeInp.getServingSize());
        foundRecipe.setIngredient(recipeInp.getIngredient());

        return recipeRepository.save(foundRecipe);

    }

    public void deleteRecipe(Long id){
        recipeRepository.deleteById(id);
    }

    public Optional<List<Recipe>> findBySearchCriteria(Specification<Recipe> spec){
        return Optional.of(recipeRepository.findAll(spec));
    }
}
