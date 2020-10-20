package com.dataplume.HarVis.har.models;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.io.Serializable;

@Entity
public class SearchWord implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    private long sId;

    @Nullable
    private String word;

    private boolean isEvolved;

    private int searchRound;


    @ManyToOne(fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    Search search;

    public SearchWord(String word, boolean isEvolved, Search search, int searchRound) {
        this.word = word;
        this.isEvolved = isEvolved;
        this.search = search;
        this.searchRound = searchRound;
        sId = -1;
    }

    public SearchWord(long id, String word, boolean isEvolved, Search search, int searchRound) {
        this.id = id;
        this.word = word;
        this.isEvolved = isEvolved;
        this.search = search;
        this.searchRound = searchRound;
        sId = id;

    }

    public SearchWord() {
        sId = -1;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getSearchRound() {
        return searchRound;
    }

    public void setSearchRound(int searchRound) {
        this.searchRound = searchRound;
    }

    public long getsId() {
        return id;
    }

    public void setsId(long sId) {
        this.sId = sId;
    }

    @Override
    public String toString() {
        return "SearchWord{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", isEvolved=" + isEvolved +
                ", searchRound=" + searchRound +
                '}';
    }
}
