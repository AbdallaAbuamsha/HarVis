package com.dataplume.HarVis.har.models;

import com.dataplume.HarVis.har.enums.AuthorType;
import com.dataplume.HarVis.har.enums.SocialMediaType;
import com.sun.istack.Nullable;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.Entity;
import javax.persistence.Id;

//import javax.persistence.OneToMany;

@Entity
public class Author {

    @Id
    private Long id;

    @Range(min = 2, max = 1000)
    private String name;

    @UniqueElements
    private String idOnSite;

    @Nullable
    private String location;

    private SocialMediaType socialMediaType;

    private AuthorType authorType;

    private long viewsCount;

    private int videosCount;

    private long subscribersCount;

//    @OneToMany(fetch = FetchType.LAZY)
//    List<Post> posts;
//
//    @OneToMany(fetch = FetchType.LAZY)
//    List<Comment> comments;

    public Author() {
    }

    public Author(String name, @UniqueElements String idOnSite, SocialMediaType socialMediaType, AuthorType authorType) {
        this.name = name;
        this.idOnSite = idOnSite;
        this.socialMediaType = socialMediaType;
        this.authorType = authorType;
//        this.posts = new ArrayList<>();
//        this.comments = new ArrayList<>();
    }

    // no posts or comments yet
    public Author(String name, @UniqueElements String idOnSite, String location, SocialMediaType socialMediaType, AuthorType authorType, long viewsCount, int videosCount, long subscribersCount) {
        this.name = name;
        this.idOnSite = idOnSite;
        this.location = location;
        this.socialMediaType = socialMediaType;
        this.authorType = authorType;
        this.viewsCount = viewsCount;
        this.videosCount = videosCount;
        this.subscribersCount = subscribersCount;
//        this.posts = new ArrayList<>();
//        this.comments = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public SocialMediaType getSocialMediaType() {
        return socialMediaType;
    }

    public void setSocialMediaType(SocialMediaType socialMediaType) {
        this.socialMediaType = socialMediaType;
    }

    public AuthorType getAuthorType() {
        return authorType;
    }

    public void setAuthorType(AuthorType authorType) {
        this.authorType = authorType;
    }

    public long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public int getVideosCount() {
        return videosCount;
    }

    public void setVideosCount(int videosCount) {
        this.videosCount = videosCount;
    }

    public long getSubscribersCount() {
        return subscribersCount;
    }

    public void setSubscribersCount(long subscribersCount) {
        this.subscribersCount = subscribersCount;
    }

    public String getIdOnSite() {
        return idOnSite;
    }

    public void setIdOnSite(String idOnSite) {
        this.idOnSite = idOnSite;
    }
}
