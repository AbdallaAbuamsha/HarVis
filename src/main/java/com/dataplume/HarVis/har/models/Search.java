package com.dataplume.HarVis.har.models;

import com.dataplume.HarVis.har.enums.CampaignMode;
import com.dataplume.HarVis.har.enums.SocialMediaType;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Search {
    @NotNull
    private CampaignMode campaignMode;
    private boolean isNew;
    @NotNull
    private SocialMediaType socialMediaType;
    @Range(min = -1, max = 1000)
    private int maxSearchResults;
    @Range(min = -1, max = 1000)
    private int maxTotalResults;
    @Range(min = 0, max = 3)
    private int maxEvolveDepth;
    @NotEmpty
    private String searchKeywords;
    private String hashTags;

    public Search() {
        isNew = true;
    }

    public Search(CampaignMode campaignMode, Boolean isNew, SocialMediaType socialMediaType, int maxSearchResults, int maxTotalResults, int maxEvolveDepth, String searchKeywords, String hashTags) {
        this.campaignMode = campaignMode;
        this.isNew = isNew;
        this.socialMediaType = socialMediaType;
        this.maxSearchResults = maxSearchResults;
        this.maxTotalResults = maxTotalResults;
        this.maxEvolveDepth = maxEvolveDepth;
        this.searchKeywords = searchKeywords;
        this.hashTags = hashTags;
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

    public SocialMediaType getSocialMediaType() {
        return socialMediaType;
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
