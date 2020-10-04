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

//    @ExceptionHandler(value = { Exception.class })
//    public @ResponseBody
//    ResponseEntity<Map<String, Object>> handleException(Exception ex) {
//        logger.error(ex.getClass()+" - "+ex.getMessage());
//        Map<String, Object> errorInfo = new HashMap<>();
//        errorInfo.put("message", ex.getMessage());
//        errorInfo.put("status", HttpStatus.BAD_REQUEST);
//        errorInfo.put("status_code", HttpStatus.BAD_REQUEST.value());
//        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
//    }

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

//    org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'handlerExceptionResolver'
//    defined in class path resource [/web/servlet/WebMvcAutoConfiguration$EnableWebMvcConfiguration.class]: Bean instantiation via factory method failed;
//    nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.web.servlet.HandlerExceptionResolver]:
//    Factory method 'handlerExceptionResolver' threw exception; nested exception is java.lang.IllegalStateException: Ambiguous @ExceptionHandler method mapped for [class org.springframework.web.bind.MethodArgumentNotValidException]:
//    {public org.springframework.http.ResponseEntity
//    com.dataplume.HarVis.exceptions.ControllerExceptionHandler.handleValidationExceptions(org.springframework.web.bind.MethodArgumentNotValidException)
//    , public final .ResponseEntityExceptionHandler.handleException(java.lang.Exception,org.springframework.web.context.request.WebRequest) throws java.lang.Exception}

}
