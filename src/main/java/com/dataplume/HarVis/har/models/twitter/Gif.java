package com.dataplume.HarVis.har.models.twitter;

import java.util.List;

public class Gif extends Medium {
    private String thumbnailUrl;
    List<VideoVariant> variants;
    private String type;

    public Gif() {
        type = "gif";
    }

    public Gif(String thumbnailUrl, List<VideoVariant> variants) {
        this.thumbnailUrl = thumbnailUrl;
        this.variants = variants;
        this.type = "gif";
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
