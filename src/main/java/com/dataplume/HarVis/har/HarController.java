package com.dataplume.HarVis.har;

import com.dataplume.HarVis.har.models.Search;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/har")
public class HarController extends RuntimeException{

    private final HarServices harServices;

    public HarController(HarServices harServices) {
        this.harServices = harServices;
    }

    @PostMapping
    public ResponseEntity<String> startCampaign(@Valid @RequestBody Search search)
    {
        return new ResponseEntity<>(search.toString(), HttpStatus.OK);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
