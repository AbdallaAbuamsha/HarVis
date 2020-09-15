package com.dataplume.HarVis.har.models;

import com.dataplume.HarVis.har.enums.AuthorType;
import com.dataplume.HarVis.har.enums.SocialMediaType;

public class Author {
    private String name;
    private String location;
    private SocialMediaType socialMediaType;
    private AuthorType authorType;
    private int subscriptions;
    private long viewsCount;
    private int videosCount;
}
