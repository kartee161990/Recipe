package com.myorg.recipe.exception;

import com.myorg.recipe.api.dto.RecipeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RecipeNotFoundException.class)
    protected ResponseEntity<RecipeResponse> handleNotFound(RuntimeException ex, WebRequest request) {
        var response = RecipeResponse.builder()
                .message("Validation error: There is no recipe related to the provided id!")
                .responseCode(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<>(response, response.getResponseCode());
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<RecipeResponse> handleUnsupportedOperation(UnsupportedOperationException ex, WebRequest request) {

        var response = RecipeResponse.builder()
                .message("Validation error: Provided values are not match with requirements!")
                .responseCode(HttpStatus.BAD_REQUEST).build();

        return new ResponseEntity<>(response, response.getResponseCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RecipeResponse> handleUnsupportedOperation(Exception ex, WebRequest request) {

        var response = RecipeResponse.builder()
                .message("Unknown request keyword!")
                .responseCode(HttpStatus.BAD_REQUEST).build();
        log.error(ex.getLocalizedMessage());

        return new ResponseEntity<>(response, response.getResponseCode());
    }



}
