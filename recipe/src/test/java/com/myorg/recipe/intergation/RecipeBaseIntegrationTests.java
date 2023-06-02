package com.myorg.recipe.intergation;

import com.myorg.recipe.api.dto.RecipeDto;
import com.myorg.recipe.entity.Ingredient;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "integration-test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RecipeBaseIntegrationTests {
    @Autowired
    private WebTestClient webTestClient;
    @Test
    @Order(1)
    void shouldCreateNewRecipe() {
        var ingredient = new Ingredient();
        ingredient.setIngName("oil");
        ingredient.setMeasurement("50ML");

        var aRecipe = RecipeDto.builder()
                .name("Pasta")
                .instructions("Cook pasta in hot water")
                .ingredient(List.of(ingredient))
                .servingSize(2)
                .build();

        webTestClient
                .post()
                .uri("api/v1/recipe/create")
                .body(Mono.just(aRecipe), RecipeDto.class)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("message ")
                .value(containsString("Successfully created the recipe!"));
    }

    @Test
    @Order(2)
    void shouldGetAllRecipes() {
        webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("api/v1/recipe")
                        .build())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("message ")
                .value(containsString("Retrieved the recipe successfully!"))
                .jsonPath("$.data[0].name")
                .value(equalTo("pizza"))
                .jsonPath("$.data[1].name")
                .value(equalTo("Pasta"));
    }

    @Test
    @Order(3)
    void shouldUpdateRecipe() {
        var ingredient = new Ingredient();
        ingredient.setIngName("oil");
        ingredient.setMeasurement("50ML");

        var aRecipe = RecipeDto.builder()
                .name("Pasta")
                .instructions("Cook pasta in warm water") // updating the text
                .ingredient(List.of(ingredient))
                .servingSize(2)
                .build();

        webTestClient
                .put()
                .uri("api/v1/recipe/2")
                .body(Mono.just(aRecipe), RecipeDto.class)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("message ")
                .value(containsString("Successfully updated the recipe!"))
                .jsonPath("$.data.instructions")
                .value(containsString("warm"));

    }

    @Test
    @Order(4)
    void shouldDeleteRecipe() {

         webTestClient
                .delete()
                .uri("api/v1/recipe/2")
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("message ")
                .value(containsString("Deleted the recipe successfully!"));


    }

    @Test
    @Order(5)
    void shouldThrowNotFoundExceptionForUpdate() {
        var ingredient = new Ingredient();
        ingredient.setIngName("oil");
        ingredient.setMeasurement("50ML");

        var aRecipe = RecipeDto.builder()
                .name("Pasta")
                .instructions("Cook pasta in warm water") // updating the text
                .ingredient(List.of(ingredient))
                .servingSize(2)
                .build();


        webTestClient
                .put()
                .uri("api/v1/recipe/2")
                .body(Mono.just(aRecipe), RecipeDto.class)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("message ")
                .value(containsString("Validation error: There is no recipe related to the provided id!"));
    }


    @Test
    @Order(6)
    void shouldThrowNotFoundExceptionForDelete() {

        webTestClient
                .delete()
                .uri("api/v1/recipe/2")
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

}
