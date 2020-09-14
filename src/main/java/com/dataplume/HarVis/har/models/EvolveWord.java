package com.dataplume.HarVis.har.models;

import java.util.ArrayList;
import java.util.List;

public class EvolveWord {
    String word;
    List<EvolveWord> evolvedWordList;

    public EvolveWord(String word) {
        this.word = word;
        evolvedWordList = new ArrayList<>();
    }
}
