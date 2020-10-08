package com.dataplume.HarVis.har.models;

import com.dataplume.HarVis.har.enums.SocialMediaType;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;

    @Range(min = 2, max = 1000)
    private String title;

    private String description;

    private SocialMediaType socialMediaType;

    private String date;

    private long viewsCount;

    private long likesCount;

    private long dislikesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable=false)
    private SearchWord searchWord;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Comment> comments;

    public Post() {
    }

    // lite post, no comments, only data in search list (not video details page)
    public Post(String title, String description, SocialMediaType socialMediaType, Author author, String date, String id, long viewsCount, SearchWord searchWord) {
        this.title = title;
        this.description = description;
        this.socialMediaType = socialMediaType;
        this.author = author;
        this.date = date;
        this.id = id;
        this.viewsCount = viewsCount;
        this.searchWord = searchWord;

        this.comments = new ArrayList<>();
        this.likesCount = -1;
        this.dislikesCount = -1;
        this.searchWord = searchWord;
    }

    public Post(String title, String description, SocialMediaType socialMediaType, Author author, String date, String id, long viewsCount, List<Comment> comments, long likesCount, long dislikesCount, SearchWord searchWord) {
        this.title = title;
        this.description = description;
        this.socialMediaType = socialMediaType;
        this.author = author;
        this.date = date;
        this.id = id;
        this.viewsCount = viewsCount;
        this.comments = comments;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
        this.searchWord = searchWord;
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

    public String getSocialMediaType() {
        return socialMediaType.name();
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

    public long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(long likesCount) {
        this.likesCount = likesCount;
    }

    public long getDislikesCount() {
        return dislikesCount;
    }

    public void setDislikesCount(long dislikesCount) {
        this.dislikesCount = dislikesCount;
    }

    public SearchWord getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(SearchWord searchWord) {
        this.searchWord = Post.this.searchWord;
    }


    @Override
    public String toString() {
        return "Post{" +'\n'+
                "title='" + title + '\'' +'\n'+
                ", description='" + description + '\'' +'\n'+
                ", socialMediaType=" + socialMediaType +'\n'+
                ", author='" + author.getName() + '\'' +'\n'+
                ", date='" + date + '\'' +'\n'+
                ", id='" + id + '\'' +'\n'+
                ", viewsCount=" + viewsCount +'\n'+
                ", likesCount=" + likesCount +'\n'+
                ", dislikesCount=" + dislikesCount +'\n'+
                //", comments=" + comments +'\n'+
                '}';
    }
}
