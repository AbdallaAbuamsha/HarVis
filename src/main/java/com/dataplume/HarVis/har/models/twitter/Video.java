package com.dataplume.HarVis.har.models.twitter;

import java.util.List;

public class Video extends Medium {
    private String thumbnailUrl;
    private List<VideoVariant> variants;
    private float duration;
    private String type;

    public Video() {
        type= "video";
    }

    public Video(String thumbnailUrl, List<VideoVariant> variants, float duration) {
        this.thumbnailUrl = thumbnailUrl;
        this.variants = variants;
        this.duration = duration;
        this.type = "video";
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public List<VideoVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<VideoVariant> variants) {
        this.variants = variants;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
