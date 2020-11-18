package com.dataplume.HarVis.har.models.twitter;

public class Photo extends Medium{

    private String previewUrl;
    private String fullUrl;
    private String type;

    public Photo() {
        type = "photo";
    }

    public Photo(String previewUrl, String fullUrl) {
        this.previewUrl = previewUrl;
        this.fullUrl = fullUrl;
        this.type = "photo";
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
