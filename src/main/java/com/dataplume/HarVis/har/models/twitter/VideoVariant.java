package com.dataplume.HarVis.har.models.twitter;

public class VideoVariant {

    private String contentType;
    private String url;
    private Integer bitrate;

    public VideoVariant() {
    }

    public VideoVariant(String contentType, String url, Integer bitrate) {
        this.contentType = contentType;
        this.url = url;
        this.bitrate = bitrate;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }
}
