package com.myorg.recipe;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myorg.recipe.entity.Recipe;
import com.myorg.recipe.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
@Slf4j
public class RecipeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeApplication.class, args);
	}

	// Initial load some recipes for testing
	@Bean
	@Profile(value = {"test", "integration-test"})
	CommandLineRunner runner(RecipeService recipeService){
		return args -> {
			// read JSON and load json
			var mapper = new ObjectMapper();
			TypeReference<List<Recipe>> typeReference = new TypeReference<>(){};
			var inputStream = TypeReference.class.getResourceAsStream("/json/recipe.json");
			try {
				var recipes = mapper.readValue(inputStream,typeReference);
				recipes.stream().forEach(recipe -> recipeService.save(recipe));

				log.info("Recipes Saved!");
			} catch (IOException e) {
				log.info("Unable to save recipes: {}", e.getMessage());
			}
		};
	}

}
