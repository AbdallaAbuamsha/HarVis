package com.dataplume.HarVis.har.models;

import java.util.List;

public abstract class Crawler {

    protected SearchWord searchWord;

    public Crawler(SearchWord searchWord)
    {
        this.searchWord = searchWord;
    }

    public void setSearchWord(SearchWord searchWord) {
        this.searchWord = searchWord;
    }
    public abstract List<Post> getData();
    public abstract String getTitle(Object o);
    public abstract String getDescription(Object o);
    public abstract String getDate(Object o);
    public abstract Author getAuthor(Object o);
    public abstract String getId(Object o);
    public abstract long getViewsCount(Object o);
    public abstract long getLikesCount(Object o);
    public abstract long getDisLikesCount(Object o);
    public abstract List<Comment> getComments(Object o);
}
