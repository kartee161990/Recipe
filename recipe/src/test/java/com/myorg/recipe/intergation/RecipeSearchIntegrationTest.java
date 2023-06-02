package com.myorg.recipe.intergation;

import com.myorg.recipe.api.dto.RecipeDto;
import com.myorg.recipe.entity.Ingredient;
import com.myorg.recipe.repository.RecipeRepository;
import com.myorg.recipe.search.RecipeSearchDto;
import com.myorg.recipe.search.RecipeSpecificationBuilder;
import com.myorg.recipe.search.SearchCriteria;
import org.junit.Before;
import org.junit.jupiter.api.*;
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
public class RecipeSearchIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    @Order(2)
    void shouldFindRecipeByVegetarian() {
        RecipeSpecificationBuilder builder = new RecipeSpecificationBuilder();
        var criteria = new SearchCriteria("isVegetarian", "with", true);

        var aRecipeSearch = new RecipeSearchDto();
        aRecipeSearch.setSearchCriteriaList(List.of(criteria));

        var recipeCreationResponse = webTestClient
                .post()
                .uri("api/v1/recipe/search")
                .body(Mono.just(aRecipeSearch), RecipeSearchDto.class)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("message ")
                .value(containsString("Successfully retried the recipes."))
                .jsonPath("$.data[0].isVegetarian")
                .value(equalTo(true))
                .jsonPath("$.data.size()")
                .value(equalTo(1));


    }

    @Test
    @Order(3)
    void shouldFindRecipeByNoOfServings() {
        var criteria = new SearchCriteria("servingSize", "eq", 2);

        var aRecipeSearch = new RecipeSearchDto();
        aRecipeSearch.setSearchCriteriaList(List.of(criteria));

        webTestClient
                .post()
                .uri("api/v1/recipe/search")
                .body(Mono.just(aRecipeSearch), RecipeSearchDto.class)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("message ")
                .value(containsString("Successfully retried the recipes."))
                .jsonPath("$.data.size()")
                .value(equalTo(1))
                .jsonPath("$.data[0].servingSize")
                .value(equalTo(2));


    }

    @Test
    @Order(4)
    void shouldFindRecipeByIngredient() {
        var criteria = new SearchCriteria("ingName", "eq",  "cheese");

        var aRecipeSearch = new RecipeSearchDto();
        aRecipeSearch.setSearchCriteriaList(List.of(criteria));

        webTestClient
                .post()
                .uri("api/v1/recipe/search")
                .body(Mono.just(aRecipeSearch), RecipeSearchDto.class)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("message ")
                .value(containsString("Successfully retried the recipes."))
                .jsonPath("$.data.size()")
                .value(equalTo(1))
                .jsonPath("$.data[0].ingredient[0].ingName")
                .value(equalTo("sauce"));


    }

    @Test
    @Order(5)
    void shouldFindRecipeByInstructionsAndIngredient() {
        var ingCriteria = new SearchCriteria("ingName", "eq",  "dough");
        var insCriteria = new SearchCriteria("instructions", "cn",  "oven");

        var aRecipeSearch = new RecipeSearchDto();
        aRecipeSearch.setSearchCriteriaList(List.of(ingCriteria, insCriteria));


        webTestClient
                .post()
                .uri("api/v1/recipe/search")
                .body(Mono.just(aRecipeSearch), RecipeSearchDto.class)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("message ")
                .value(containsString("Successfully retried the recipes."))
                .jsonPath("$.data.size()")
                .value(equalTo(1))
                .jsonPath("$.data[0].instructions")
                .value(containsString("oven"))
                .jsonPath("$.data[0].ingredient[0].ingName")
                .value(equalTo("sauce"));


    }

}
