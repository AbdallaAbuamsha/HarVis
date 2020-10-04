package com.dataplume.HarVis.spark.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/spark")
public class SparkController {

    @Autowired
    SparkService sparkService;

    @GetMapping
    public Map<String, Long> wordCount(@RequestParam(required = true) String words)
    {
        List<String> wordList = Arrays.asList(words.split(";"));
        return sparkService.getCount(wordList);
    }
}
