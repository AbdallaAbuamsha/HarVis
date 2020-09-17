package com.dataplume.HarVis.har.models;

import java.util.List;

public abstract class Crawler {

    protected Search search;

    public Crawler(Search search)
    {
        this.search = search;
    }

    public abstract List<Post> getData();
    public abstract String getTitle(Object o);
    public abstract String getDescription(Object o);
    public abstract String getDate(Object o);
    public abstract String getPublisher(Object o);
    public abstract String getId(Object o);
    public abstract long getViewsCount(Object o);
    public abstract long getLikesCount(Object o);
    public abstract long getDisLikesCount(Object o);
    public abstract List<Comment> getComments(Object o);
}
