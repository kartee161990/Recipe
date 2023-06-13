package com.myorg.recipe.controller;

import com.myorg.recipe.api.dto.RecipeDto;
import com.myorg.recipe.api.dto.RecipeResponse;
import com.myorg.recipe.entity.Recipe;
import com.myorg.recipe.exception.RecipeNotFoundException;
import com.myorg.recipe.search.RecipeSearchDto;
import com.myorg.recipe.search.RecipeSpecificationBuilder;
import com.myorg.recipe.service.RecipeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/recipe")
public class RecipeController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RecipeService recipeService;

    @GetMapping(value = { "", "/" })
    public ResponseEntity<RecipeResponse> getRecipes() {
        var response = RecipeResponse.builder()
                .data(recipeService.getAllRecipes())
                .message("Retrieved the recipe successfully!")
                .responseCode(HttpStatus.OK).build();

        return new ResponseEntity<>(response, response.getResponseCode());
    }


    @PostMapping(value = "/create")
    public ResponseEntity<RecipeResponse> addRecipe(@RequestBody RecipeDto recipe) {
        var response = RecipeResponse.builder()
                .data(recipeService.save(modelMapper.map(recipe, Recipe.class)))
                .message("Successfully created the recipe!")
                .responseCode(HttpStatus.OK).build();

        return new ResponseEntity<>(response, response.getResponseCode());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponse> updateRecipe(@PathVariable Long id, @RequestBody RecipeDto recipe) {
        return  recipeService.findByRecipeId(id)
                .map(currentRecipe -> {
                    var response = RecipeResponse.builder()
                            .data(recipeService.updateRecipe(currentRecipe, modelMapper.map(recipe, Recipe.class)))
                            .message("Successfully updated the recipe!")
                            .responseCode(HttpStatus.OK).build();

                    return new ResponseEntity<>(response, response.getResponseCode());
                }).orElseThrow(RecipeNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RecipeResponse> deleteRecipe(@PathVariable Long id) {
        return recipeService.findByRecipeId(id).
                map(recipe -> {
                    recipeService.deleteRecipe(recipe.getId());

                    var response = RecipeResponse.builder()
                            .message("Deleted the recipe successfully!")
                            .responseCode(HttpStatus.OK).build();

                    return new ResponseEntity<>(response, HttpStatus.OK);
                }).orElseThrow(RecipeNotFoundException::new);

    }

    @PostMapping(value = "/search")
    public ResponseEntity<RecipeResponse> searchRecipes(@RequestBody RecipeSearchDto recipeSearchDto) {

        var builder = new RecipeSpecificationBuilder();
        var criteriaList = recipeSearchDto.getSearchCriteriaList();

        if (recipeSearchDto.getDataOption() != null) {
            builder.with(recipeSearchDto.getDataOption());
        }
        if (criteriaList != null) {
            criteriaList.forEach(builder::with);
        }

        var recipe = recipeService.findBySearchCriteria(builder.build());
        var response = RecipeResponse.builder()
                .data(recipe.isPresent() ? recipe.get() : List.of())
                .message("Successfully retried the recipes.")
                .responseCode(HttpStatus.OK).build();

        return new ResponseEntity<>(response, response.getResponseCode());
    }

}
