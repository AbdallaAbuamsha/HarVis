package com.dataplume.HarVis.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextProcessing {

    public static List<String> WORD_2B_FILTERED;
    private final String DELIM = " _-=+&\t\n.,:;?!@#$%^&*~/{}()[]\"\'1234567890";
    public static void fillWordsToBeFiltered()
    {
        try
        {
            File wordsToBeFilteredFile = new ClassPathResource("static/word_2b_filtered").getFile();
            BufferedReader wordsToBeFilteredBuffer = new BufferedReader(new FileReader(wordsToBeFilteredFile));
            String currentReadingLine = wordsToBeFilteredBuffer.readLine();
            StringBuilder wordsToBeFiltered = new StringBuilder();
            while (currentReadingLine != null) {
                wordsToBeFiltered.append(currentReadingLine);
                currentReadingLine = wordsToBeFilteredBuffer.readLine();
            }
            WORD_2B_FILTERED = Arrays.asList(wordsToBeFiltered.toString().split(","));
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
    public static String removeStopWords(String text)
    {
        if(WORD_2B_FILTERED == null)
            fillWordsToBeFiltered();

        ArrayList<String> textWordsList =
                Stream.of(text.toLowerCase().split("\\s+"))
                        .collect(Collectors.toCollection(ArrayList::new));

        textWordsList.removeAll(WORD_2B_FILTERED);

        return String.join(" ", textWordsList);
    }

    public static String removeNoneLetters(String title) {
        return  title.replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit}]", " ")
                .replaceAll(" +", " ")
                .trim();
    }

    public static String removeOneLetterWords(String title) {
        return title.replaceAll("(?>^[\\p{L}\\p{N}]\\s+|(\\s+[\\p{L}\\p{N}])+$)"," ")
                .replaceAll(" +", " ");
    }

    public static List<String> getWordsListFromStringList(List<String> stirngList) {
        return stirngList
                .stream()
                .map(s -> Arrays.stream(s.split(" "))
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static Map<String, Long> getWordsCount(List<String> titles) {
        return titles
                .stream()
                .collect(Collectors.groupingBy(t -> t, Collectors.counting()));
    }
}
