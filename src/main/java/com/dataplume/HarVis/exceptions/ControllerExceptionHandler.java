package com.dataplume.HarVis.exceptions;

import com.dataplume.HarVis.auth.AuthInitializer;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import javax.xml.bind.ValidationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    Logger logger = LoggerFactory.getLogger(AuthInitializer.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentsValidationExceptions(MethodArgumentNotValidException ex) {
        logger.error("Invalid Input MethodArgumentNotValidException: ",ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(value = { ValidationException.class })
    public ResponseEntity<Object> handleInvalidInputException(ValidationException ex) {
        logger.error("Invalid Input Exception: ",ex.getMessage());
        return new ResponseEntity<Object>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { HttpClientErrorException.Unauthorized.class })
    public ResponseEntity<Object> handleUnauthorizedException(HttpClientErrorException.Unauthorized ex) {
        logger.error("Unauthorized Exception: ",ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = { Exception.class  })
    public ResponseEntity<Object> handleException(Exception ex) {
        logger.error("Exception: ",ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value={JwtException.class, AccessDeniedException.class})
    public ResponseEntity<Object> handleJwtException(JwtException ex) {
        logger.error(ex.getClass()+" - "+ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
