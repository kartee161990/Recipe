package com.myorg.recipe.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class RecipeResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private String message;
    private HttpStatusCode responseCode;

}
