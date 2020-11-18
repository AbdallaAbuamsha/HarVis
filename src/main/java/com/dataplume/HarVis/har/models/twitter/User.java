package com.dataplume.HarVis.har.models.twitter;

import java.util.Date;
import java.util.List;

public class User {

    private String username;
    private String displayname;
    private String id; // Seems to always be numeric, but the API returns it as a string, so it might also contain other things in the future
    private String description; // Description as it's displayed on the web interface with URLs replaced
    private String rawDescription; // Raw description with the URL(s) intact
    private List<DescriptionURL> descriptionUrls;
    private Boolean verified;
    private Date created;
    private Integer followersCount;
    private Integer friendsCount;
    private Integer statusesCount;
    private Integer favouritesCount;
    private Integer listedCount;
    private Integer mediaCount;
    private String location;
    private Boolean protected_;
    private String linkUrl;
    private String linkTcourl;
    private String profileImageUrl;
    private String profileBannerUrl;

    public User(String username, String displayname, String id, String description, String rawDescription, List<DescriptionURL> descriptionUrls, Boolean verified, Date created, Integer followersCount, Integer friendsCount, Integer statusesCount, Integer favouritesCount, Integer listedCount, Integer mediaCount, String location, Boolean protected_, String linkUrl, String linkTcourl, String profileImageUrl, String profileBannerUrl) {
        this.username = username;
        this.displayname = displayname;
        this.id = id;
        this.description = description;
        this.rawDescription = rawDescription;
        this.descriptionUrls = descriptionUrls;
        this.verified = verified;
        this.created = created;
        this.followersCount = followersCount;
        this.friendsCount = friendsCount;
        this.statusesCount = statusesCount;
        this.favouritesCount = favouritesCount;
        this.listedCount = listedCount;
        this.mediaCount = mediaCount;
        this.location = location;
        this.protected_ = protected_;
        this.linkUrl = linkUrl;
        this.linkTcourl = linkTcourl;
        this.profileImageUrl = profileImageUrl;
        this.profileBannerUrl = profileBannerUrl;
    }

    public User(String displayname, String username, String id) {
        this.username = username;
        this.displayname = displayname;
        this.id = id;
    }



    public User() {
    }

    public String url() {
        return "https://twitter.com/{}".formatted(this.username);
    }

    @Override
    public String toString() {
        return url();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRawDescription() {
        return rawDescription;
    }

    public void setRawDescription(String rawDescription) {
        this.rawDescription = rawDescription;
    }

    public List<DescriptionURL> getDescriptionUrls() {
        return descriptionUrls;
    }

    public void setDescriptionUrls(List<DescriptionURL> descriptionUrls) {
        this.descriptionUrls = descriptionUrls;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(Integer friendsCount) {
        this.friendsCount = friendsCount;
    }

    public Integer getStatusesCount() {
        return statusesCount;
    }

    public void setStatusesCount(Integer statusesCount) {
        this.statusesCount = statusesCount;
    }

    public Integer getFavouritesCount() {
        return favouritesCount;
    }

    public void setFavouritesCount(Integer favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public Integer getListedCount() {
        return listedCount;
    }

    public void setListedCount(Integer listedCount) {
        this.listedCount = listedCount;
    }

    public Integer getMediaCount() {
        return mediaCount;
    }

    public void setMediaCount(Integer mediaCount) {
        this.mediaCount = mediaCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getProtected_() {
        return protected_;
    }

    public void setProtected_(Boolean protected_) {
        this.protected_ = protected_;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkTcourl() {
        return linkTcourl;
    }

    public void setLinkTcourl(String linkTcourl) {
        this.linkTcourl = linkTcourl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getProfileBannerUrl() {
        return profileBannerUrl;
    }

    public void setProfileBannerUrl(String profileBannerUrl) {
        this.profileBannerUrl = profileBannerUrl;
    }
}
