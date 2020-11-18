package com.dataplume.HarVis.har.models.twitter;

import java.util.List;

public class Tweet {

    private String url;
    private String date;
    private String content;
    private String renderedContent;
    private int id;
    private String username; // Deprecated, use user['username'] instead
    List<Object>outlinks;//List<Outlinks>outlinks;
    private String outlinksss; // Deprecated, use outlinks instead
    List<Object>tcooutlinks;//List<Tcooutlinks>tcooutlinks;
    private String tcooutlinksss; // Deprecated, use tcooutlinks instead
    private int replyCount;
    private int retweetCount;
    private int likeCount;
    private int quoteCount;
    private int conversationId;
    private String lang;
    private String source;
    private List<Medium> media;
    private Tweet retweetedTweet;
    private Tweet quotedTweet;
    private User mentionedUsers;

    @Override
    public String toString() {
        return url;
    }
}
