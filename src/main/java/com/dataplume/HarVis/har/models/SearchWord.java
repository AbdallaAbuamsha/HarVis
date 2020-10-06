package com.dataplume.HarVis.har.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class SearchWord {
    @Id
    private Long id;

    String word;

    boolean isEvolved;

    int searchRound;


    @ManyToOne(fetch = FetchType.LAZY)
    Search search;

    public SearchWord(String word, boolean isEvolved, Search search, int searchRound) {
        this.word = word;
        this.isEvolved = isEvolved;
        this.search = search;
        this.searchRound = searchRound;
    }

    public SearchWord() {

    }

    public Long getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isEvolved() {
        return isEvolved;
    }

    public void setEvolved(boolean evolved) {
        isEvolved = evolved;
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public String getFullSearchWords()
    {
        return (isEvolved)?search.getSearchKeywords() + " " + word:word;
    }
}
