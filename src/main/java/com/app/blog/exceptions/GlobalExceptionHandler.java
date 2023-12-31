package com.app.blog.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.blog.payloads.APIResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){

        String msg = ex.getMessage();
        APIResponse apiResponse = new APIResponse(msg,false);
        return new ResponseEntity<APIResponse>(apiResponse,HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Map<String,String>> handleMethodArgNotValidException(MethodArgumentNotValidException ex){

        Map<String, String> resp = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            resp.put(fieldName, message);
        });

        return new ResponseEntity<Map<String, String>>(resp, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<APIResponse> apiExceptionHandler(ApiException ex){

        String msg = ex.getMessage();
        APIResponse apiResponse = new APIResponse(msg,false);
        return new ResponseEntity<APIResponse>(apiResponse,HttpStatus.BAD_REQUEST);
    }
}
