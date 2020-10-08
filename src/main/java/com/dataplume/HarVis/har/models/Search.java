package com.dataplume.HarVis.har.models;

import com.dataplume.HarVis.auth.models.User;
import com.dataplume.HarVis.har.enums.CampaignMode;
import com.dataplume.HarVis.har.enums.SocialMediaType;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Search {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @NotNull
    private CampaignMode campaignMode;

    private boolean isNew;

    @NotNull
    private SocialMediaType socialMediaType;

    // -1 to get everything
    @Range(min = -1, max = 1000)
    private int maxSearchResults;

    // -1 to get everything
    @Range(min = -1, max = 1000)
    private int maxTotalResults;

    // 0 to not evolve at all, just retrieve search data
    @Range(min = 0, max = 3)
    private int maxEvolveDepth;

    @NotNull
    private String searchKeywords;

    private String hashTags;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date time;

//    @OneToMany(fetch = FetchType.LAZY)
//    private List<Post> posts;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Search() {
        isNew = true;
        this.time = new java.util.Date();
        //this.posts = new ArrayList<>();
    }

    public Search(CampaignMode campaignMode, Boolean isNew, SocialMediaType socialMediaType, int maxSearchResults, int maxTotalResults, int maxEvolveDepth, String searchKeywords, String hashTags, User user) {
        this.campaignMode = campaignMode;
        this.isNew = isNew;
        this.socialMediaType = socialMediaType;
        this.maxSearchResults = maxSearchResults;
        this.maxTotalResults = maxTotalResults;
        this.maxEvolveDepth = maxEvolveDepth;
        this.searchKeywords = searchKeywords;
        this.hashTags = hashTags;
        this.user = user;
        this.time = new java.util.Date();
        //this.posts = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public CampaignMode getCampaignMode() {
        return campaignMode;
    }

    public void setCampaignMode(CampaignMode campaignMode) {
        this.campaignMode = campaignMode;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public SocialMediaType getSocialMediaTypeEnum() {
        return socialMediaType;
    }
    public String getSocialMediaType() {
        return socialMediaType.name();
    }

    public void setSocialMediaType(SocialMediaType socialMediaType) {
        this.socialMediaType = socialMediaType;
    }

    public int getMaxSearchResults() {
        return maxSearchResults;
    }

    public void setMaxSearchResults(int maxSearchResults) {
        this.maxSearchResults = maxSearchResults;
    }

    public int getMaxTotalResults() {
        return maxTotalResults;
    }

    public void setMaxTotalResults(int maxTotalResults) {
        this.maxTotalResults = maxTotalResults;
    }

    public int getMaxEvolveDepth() {
        return maxEvolveDepth;
    }

    public void setMaxEvolveDepth(int maxEvolveDepth) {
        this.maxEvolveDepth = maxEvolveDepth;
    }

    public String getSearchKeywords() {
        return searchKeywords;
    }

    public void setSearchKeywords(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

    public String getHashTags() {
        return hashTags;
    }

    public void setHashTags(String hashTags) {
        this.hashTags = hashTags;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

//    public List<Post> getPosts() {
//        return posts;
//    }
//
//    public void setPosts(List<Post> posts) {
//        this.posts = posts;
//    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Search{" + "\n"+
                "  campaignMode=" + campaignMode + "\n"+
                ", isNew=" + isNew + "\n"+
                ", socialMediaType=" + socialMediaType + "\n"+
                ", maxSearchResults=" + maxSearchResults + "\n"+
                ", maxTotalResults=" + maxTotalResults + "\n"+
                ", maxEvolveDepth=" + maxEvolveDepth + "\n"+
                ", searchKeywords=" + searchKeywords + "\n"+
                ", hashTags='" + hashTags + "\n"+
                '}';
    }
}
