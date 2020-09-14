package com.dataplume.HarVis.har.models.crawlers;

import com.dataplume.HarVis.har.models.Crawler;
import com.dataplume.HarVis.har.models.Post;
import com.dataplume.HarVis.har.models.Search;

import java.util.List;

public class YoutubeCrawler extends Crawler {

    public YoutubeCrawler(Search search) {
        super(search);
    }

    @Override
    public List<Post> getData() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getDate() {
        return null;
    }

    @Override
    public String getPublisher() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public long getViewsCount() {
        return 0;
    }

    @Override
    public long getLikesCount() {
        return 0;
    }

    @Override
    public long getDisLikesCount() {
        return 0;
    }
}
