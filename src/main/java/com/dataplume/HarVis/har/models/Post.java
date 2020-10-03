package com.dataplume.HarVis.har.models;

import com.dataplume.HarVis.har.enums.SocialMediaType;

import java.util.List;

public class Post {
    private String title;
    private String description;
    private SocialMediaType socialMediaType;
    private String publisher;
    private String date;
    private String id;
    private long viewsCount;
    private long likesCount;
    private long dislikesCount;

    private List<Comment> comments;

    public Post() {
    }

    public Post(String title, String description, SocialMediaType socialMediaType, String publisher, String date, String id, long viewsCount) {
        this.title = title;
        this.description = description;
        this.socialMediaType = socialMediaType;
        this.publisher = publisher;
        this.date = date;
        this.id = id;
        this.viewsCount = viewsCount;
        this.comments = null;
        this.likesCount = -1;
        this.dislikesCount = -1;
    }

    public Post(String title, String description, SocialMediaType socialMediaType, String publisher, String date, String id, long viewsCount, List<Comment> comments, long likesCount, long dislikesCount) {
        this.title = title;
        this.description = description;
        this.socialMediaType = socialMediaType;
        this.publisher = publisher;
        this.date = date;
        this.id = id;
        this.viewsCount = viewsCount;
        this.comments = comments;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
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

    @Override
    public String toString() {
        return "Post{" +'\n'+
                "title='" + title + '\'' +'\n'+
                ", description='" + description + '\'' +'\n'+
                ", socialMediaType=" + socialMediaType +'\n'+
                ", publisher='" + publisher + '\'' +'\n'+
                ", date='" + date + '\'' +'\n'+
                ", id='" + id + '\'' +'\n'+
                ", viewsCount=" + viewsCount +'\n'+
                ", likesCount=" + likesCount +'\n'+
                ", dislikesCount=" + dislikesCount +'\n'+
                ", comments=" + comments +'\n'+
                '}';
    }
}
