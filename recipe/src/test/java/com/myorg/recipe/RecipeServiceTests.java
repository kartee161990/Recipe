package com.myorg.recipe;

import com.myorg.recipe.entity.Ingredient;
import com.myorg.recipe.entity.Recipe;
import com.myorg.recipe.repository.RecipeRepository;
import com.myorg.recipe.search.RecipeSearchDto;
import com.myorg.recipe.search.RecipeSpecification;
import com.myorg.recipe.search.SearchCriteria;
import com.myorg.recipe.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTests {
    @InjectMocks
    RecipeService recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Test
    public void givenRecipes_whenFindRecipes_thenReturnRecipes() {

        var ingredients = new Ingredient();
        ingredients.setIngName("cheese");
        ingredients.setMeasurement("100G");

        var recipe = new Recipe();
        recipe.setServingSize(2);
        recipe.setName("Pasta");
        recipe.setIngredient(List.of(ingredients));
        recipe.setIsVegetarian(true);

        when(recipeRepository.findAll()).thenReturn(List.of(recipe));
        var results = recipeService.getAllRecipes();

        assertThat(results.get(0).getName()).isEqualTo("Pasta");
        verify(recipeRepository, times(1)).findAll();

    }

    @Test
    public void givenRecipe_whenSaveRecipe_thenReturnCreatedRecipe() {

        var ingredients = new Ingredient();
        ingredients.setIngName("dough");
        ingredients.setMeasurement("100G");

        var recipe = new Recipe();
        recipe.setId(null);
        recipe.setName("pizza");
        recipe.setServingSize(2);
        recipe.setIngredient(List.of(ingredients));
        recipe.setIsVegetarian(true);

        when(recipeRepository.save(recipe)).thenReturn(recipe);

        var results = recipeService.save(recipe);

        assertThat(results.getName()).isEqualTo("pizza");
        verify(recipeRepository, times(1)).save(recipe);
    }

    @Test
    public void givenRecipeAndId_whenUpdateRecipe_thenReturnSuccessWithNewRecipe() {

        var ingredients = new Ingredient();
        ingredients.setIngName("dough");
        ingredients.setMeasurement("100G");

        var recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("pasta");
        recipe.setServingSize(2);
        recipe.setIngredient(List.of(ingredients));
        recipe.setIsVegetarian(true);

        when(recipeRepository.save(recipe)).thenReturn(recipe);

        var results = recipeService.updateRecipe(recipe, recipe);

        assertThat(results.getName()).isEqualTo("pasta");
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }

    @Test
    public void givenRecipe_whenSearchRecipeByInstructionAndIngredient_thenReturnMatchedRecipe() {
        var searchCriteriaInstructions = new SearchCriteria();
        searchCriteriaInstructions.setFilterKey("instructions");
        searchCriteriaInstructions.setOperation("cn");
        searchCriteriaInstructions.setValue("bake");

        var searchCriteriaIngredient = new SearchCriteria();
        searchCriteriaIngredient.setFilterKey("ingredient");
        searchCriteriaIngredient.setOperation("eq");
        searchCriteriaIngredient.setValue("dough");

        var recipeRequest = new RecipeSearchDto();
        recipeRequest.setSearchCriteriaList(List.of(searchCriteriaInstructions, searchCriteriaIngredient));

        var ingredients = new Ingredient();
        ingredients.setIngName("dough");
        ingredients.setMeasurement("100G");

        var recipe = new Recipe();
        recipe.setId(null);
        recipe.setName("pizza");
        recipe.setServingSize(2);
        recipe.setInstructions("bake pizza");
        recipe.setIngredient(List.of(ingredients));
        recipe.setIsVegetarian(true);

        when(recipeRepository.findAll(new RecipeSpecification(searchCriteriaIngredient))).thenReturn(List.of(recipe));

        var results = recipeService.findBySearchCriteria(new RecipeSpecification(searchCriteriaIngredient));

        assertThat(results.get().size() == 1);
        assertThat(results.get().get(0).getIngredient().get(0).getIngName()).isEqualTo("dough");
        assertThat(results.get().get(0).getInstructions()).contains("bake");
    }

}
