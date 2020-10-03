package com.dataplume.HarVis.exceptions;

import com.dataplume.HarVis.auth.RoleInitializer;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import org.springframework.security.access.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    Logger logger = LoggerFactory.getLogger(RoleInitializer.class);

    @ExceptionHandler(value = { Exception.class })
    public @ResponseBody
    ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        logger.error(ex.getClass()+" - "+ex.getMessage());
        Map<String, Object> errorInfo = new HashMap<>();
        errorInfo.put("message", ex.getMessage());
        errorInfo.put("status", HttpStatus.BAD_REQUEST);
        errorInfo.put("status_code", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value={JwtException.class, AccessDeniedException.class})
    //@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public @ResponseBody
    ResponseEntity handleJwtException(JwtException ex) {
        logger.error(ex.getClass()+" - "+ex.getMessage());
        Map<String, Object> errorInfo = new HashMap<>();
        errorInfo.put("message", ex.getLocalizedMessage());
        errorInfo.put("status", HttpStatus.UNPROCESSABLE_ENTITY);
        errorInfo.put("status_code", HttpStatus.UNPROCESSABLE_ENTITY.value());
        return new ResponseEntity<>(errorInfo, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
