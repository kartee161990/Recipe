package com.myorg.recipe;

import com.myorg.recipe.api.dto.RecipeDto;
import com.myorg.recipe.controller.RecipeController;
import com.myorg.recipe.entity.Ingredient;
import com.myorg.recipe.entity.Recipe;
import com.myorg.recipe.exception.RecipeNotFoundException;
import com.myorg.recipe.search.RecipeSearchDto;
import com.myorg.recipe.search.RecipeSpecification;
import com.myorg.recipe.search.SearchCriteria;
import com.myorg.recipe.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeControllerTests {
    @InjectMocks
    RecipeController recipeController;

    @Mock
    RecipeService recipeService;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void givenRecipes_whenGetRecipes_thenReturnRecipes() {
        var ingredients = new Ingredient();
        ingredients.setIngName("dough");
        ingredients.setMeasurement("100G");

        var recipe = new Recipe();
        recipe.setServingSize(2);
        recipe.setIngredient(List.of(ingredients));
        recipe.setIsVegetarian(true);

        when(recipeService.getAllRecipes()).thenReturn(List.of(recipe));
        var results = recipeController.getRecipes();

        assertThat(results.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        verify(recipeService, times(1)).getAllRecipes();

    }

    @Test
    public void givenRecipe_whenCreateRecipe_thenReturnCreatedRecipe() {
        var ingredients = new Ingredient();
        ingredients.setIngName("dough");
        ingredients.setMeasurement("100G");

        var recipe = new Recipe();
        recipe.setId(null);
        recipe.setName("pizza");
        recipe.setServingSize(2);
        recipe.setIngredient(List.of(ingredients));
        recipe.setIsVegetarian(true);

        var recipeDto = RecipeDto.builder()
                .name("pizza")
                .servingSize(2)
                .ingredient(List.of(ingredients))
                .isVegetarian(true)
                .build();

        when(modelMapper.map(recipeDto, Recipe.class)).thenReturn(recipe);
        when(recipeService.save(recipe)).thenReturn(recipe);

        var results = recipeController.addRecipe(recipeDto);

        assertThat(results.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(((Recipe)results.getBody().getData()).getName()).isEqualTo("pizza");
        verify(recipeService, times(1)).save(recipe);
        verify(modelMapper, times(1)).map(recipeDto, Recipe.class);
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

        var recipeDto = RecipeDto.builder()
                .name("pasta")
                .servingSize(2)
                .ingredient(List.of(ingredients))
                .isVegetarian(true)
                .build();

        when(modelMapper.map(recipeDto, Recipe.class)).thenReturn(recipe);
        when(recipeService.findByRecipeId(1L)).thenReturn(Optional.of(recipe));
        when(recipeService.updateRecipe(recipe, recipe)).thenReturn(recipe);

        var results = recipeController.updateRecipe(1L, recipeDto);

        assertThat(results.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(((Recipe)results.getBody().getData()).getName()).isEqualTo("pasta");
        verify(recipeService, times(1)).findByRecipeId(1L);
        verify(recipeService, times(1)).updateRecipe(recipe, recipe);
        verify(modelMapper, times(1)).map(recipeDto, Recipe.class);
    }

    @Test
    public void givenRecipe_whenSearchRecipeByName_thenReturnMatchedRecipe() {
        var searchCriteria = new SearchCriteria();
        searchCriteria.setFilterKey("name");
        searchCriteria.setOperation("eq");
        searchCriteria.setValue("pizza");

        var recipeRequest = new RecipeSearchDto();
        recipeRequest.setSearchCriteriaList(List.of(searchCriteria));

        var ingredients = new Ingredient();
        ingredients.setIngName("dough");
        ingredients.setMeasurement("100G");

        var recipe = new Recipe();
        recipe.setId(null);
        recipe.setName("pizza");
        recipe.setServingSize(2);
        recipe.setIngredient(List.of(ingredients));
        recipe.setIsVegetarian(true);

        when(recipeService.findBySearchCriteria(any(RecipeSpecification.class))).thenReturn(Optional.of(List.of(recipe)));

        var results = recipeController.searchRecipes(recipeRequest);

        assertThat(results.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(((List<Recipe>)results.getBody().getData()).size() == 1);
        assertThat(((List<Recipe>)results.getBody().getData()).get(0).getName()).isEqualTo("pizza");
        verify(recipeService, times(1)).findBySearchCriteria(any(RecipeSpecification.class));
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


        when(recipeService.findBySearchCriteria(any(Specification.class))).thenReturn(Optional.of(List.of(recipe)));

        var results = recipeController.searchRecipes(recipeRequest);

        assertThat(results.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(((List<Recipe>)results.getBody().getData()).size() == 1);
        assertThat(((List<Recipe>)results.getBody().getData()).get(0).getIngredient().get(0).getIngName()).isEqualTo("dough");
        assertThat(((List<Recipe>)results.getBody().getData()).get(0).getInstructions()).contains("bake");
        verify(recipeService, times(1)).findBySearchCriteria(any(Specification.class));
    }

    @Test
    public void whenExceptionThrown_thenAssertionSucceeds() {
        var exception = assertThrows(RecipeNotFoundException.class, () -> {
            var ingredients = new Ingredient();
            ingredients.setIngName("dough");
            ingredients.setMeasurement("100G");

            var recipe = new Recipe();
            recipe.setId(1L);
            recipe.setName("pasta");
            recipe.setServingSize(2);
            recipe.setIngredient(List.of(ingredients));
            recipe.setIsVegetarian(true);

            var recipeDto = RecipeDto.builder()
                    .name("pasta")
                    .servingSize(2)
                    .ingredient(List.of(ingredients))
                    .isVegetarian(true)
                    .build();

            when(recipeService.findByRecipeId(1L)).thenThrow(new RecipeNotFoundException());
            recipeController.updateRecipe(1L, recipeDto);
        });

        assertTrue(exception instanceof RecipeNotFoundException);
    }
}
