package com.dataplume.HarVis.har.models;

import com.dataplume.HarVis.har.enums.AuthorType;
import com.dataplume.HarVis.har.enums.SocialMediaType;
import com.sun.istack.Nullable;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.io.Serializable;

//import javax.persistence.OneToMany;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"idOnSite", "socialMediaType"})
})
public class Author implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

//    @Size(min = 2, max = 1000)
    private String name;

    private String idOnSite;

    @Nullable
    private String location;

    @Enumerated(EnumType.STRING)
    private SocialMediaType socialMediaType;

    @Enumerated(EnumType.STRING)
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

    public void setId(Long id) {
        this.id = id;
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

    public String getSocialMediaType() {
        return socialMediaType.name();
    }

    public void setSocialMediaType(String socialMediaType) {
        this.socialMediaType = SocialMediaType.valueOf(socialMediaType);
    }

    public String getAuthorType() {
        return authorType.name();
    }

    public void setAuthorType(String authorType) {
        this.authorType = AuthorType.valueOf(authorType);
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

    @Override
    public String toString() {
        return idOnSite;
    }
}
