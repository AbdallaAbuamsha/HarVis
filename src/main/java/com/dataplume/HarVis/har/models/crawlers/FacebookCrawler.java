package com.dataplume.HarVis.har.models.crawlers;

import com.dataplume.HarVis.har.models.Crawler;
import com.dataplume.HarVis.har.models.Post;
import com.dataplume.HarVis.har.models.Search;

import java.util.List;

public abstract class FacebookCrawler extends Crawler{

    public FacebookCrawler(Search search) {
        super(search);
    }
}
