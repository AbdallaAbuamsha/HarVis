package com.dataplume.HarVis.har.models;

import com.dataplume.HarVis.har.enums.SocialMediaType;

import java.util.List;

public class Post {
    private String title;
    private String description;
    private SocialMediaType socialMediaType;
    private Author author;
    private String date;
    private String id;
    private long viewsCount;
    private List<Comment> comments;

    public Post() {
    }

    public Post(String title, String description, SocialMediaType socialMediaType, Author author, String date, String id, long viewsCount, List<Comment> comments) {
        this.title = title;
        this.description = description;
        this.socialMediaType = socialMediaType;
        this.author = author;
        this.date = date;
        this.id = id;
        this.viewsCount = viewsCount;
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SocialMediaType getSocialMediaType() {
        return socialMediaType;
    }

    public void setSocialMediaType(SocialMediaType socialMediaType) {
        this.socialMediaType = socialMediaType;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
