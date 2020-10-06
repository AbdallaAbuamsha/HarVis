package com.dataplume.HarVis.har.models.crawlers;

import com.dataplume.HarVis.har.models.Crawler;
import com.dataplume.HarVis.har.models.Post;
import com.dataplume.HarVis.har.models.Search;
import com.dataplume.HarVis.har.models.SearchWord;

import java.util.List;

public abstract class FacebookCrawler extends Crawler{

    public FacebookCrawler(SearchWord searchWord) {
        super(searchWord);
    }
}
