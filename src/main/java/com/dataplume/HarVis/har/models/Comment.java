package com.dataplume.HarVis.har.models;

public class Comment {
    private Comment parent;
    private String comment;
    private Author author;

    public Comment() {
    }

    // constructor for test only
    public Comment(String comment) {
        this.comment = comment;
        this.author = null;
        this.author = null;
    }

    public Comment(Comment parent, String comment, Author author) {
        this.parent = parent;
        this.comment = comment;
        this.author = author;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
