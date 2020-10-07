package com.dataplume.HarVis.har.models;

import java.util.ArrayList;
import java.util.List;

public abstract class Crawler {

    protected SearchWord searchWord;
    protected List<Post> postsList;
    protected List<Author> authorsList;
    protected List<Comment> commentsList;
    public Crawler()
    {
    }

    public void getData(SearchWord searchWord)
    {
        this.searchWord = searchWord;
        postsList = new ArrayList<>();
        authorsList = new ArrayList<>();
        commentsList = new ArrayList<>();
        getData();
    }

    protected abstract void getData();
    public List<Post> getPosts() {return postsList;}
    public List<Author> getAuthors() {return authorsList;}
    public List<Comment> getComments() {return commentsList;};
    public abstract String getTitle(Object o);
    public abstract String getDescription(Object o);
    public abstract String getDate(Object o);
    public abstract Author getAuthor(Object o);
    public abstract String getId(Object o);
    public abstract long getViewsCount(Object o);
    public abstract long getLikesCount(Object o);
    public abstract long getDisLikesCount(Object o);
    public abstract List<Comment> getPostComments(Object o);
}
