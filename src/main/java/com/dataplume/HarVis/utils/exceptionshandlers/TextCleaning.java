package com.dataplume.HarVis.utils.exceptionshandlers;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextCleaning {

    public static List<String> WORD_2B_FILTERED;

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
}
