package com.dataplume.HarVis.har.models;

import java.util.List;

public abstract class Crawler {

    protected Search search;

    public Crawler(Search search)
    {
        this.search = search;
    }

    public abstract List<Post> getData();
    public abstract String getTitle();
    public abstract String getDescription();
    public abstract String getDate();
    public abstract String getPublisher();
    public abstract String getId();
    public abstract long getViewsCount();
    public abstract long getLikesCount();
    public abstract long getDisLikesCount();
}
