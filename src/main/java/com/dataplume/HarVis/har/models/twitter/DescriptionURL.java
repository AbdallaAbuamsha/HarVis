package com.dataplume.HarVis.har.models.twitter;

public class DescriptionURL {
    private String text;
    private String url;
    private String tcourl;
    private int[] indices;

    public DescriptionURL() {
    }

    public DescriptionURL(String text, String url, String tcourl, int[] indices) {
        this.text = text;
        this.url = url;
        this.tcourl = tcourl;
        this.indices = indices;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTcourl() {
        return tcourl;
    }

    public void setTcourl(String tcourl) {
        this.tcourl = tcourl;
    }

    public int[] getIndices() {
        return indices;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }
}
