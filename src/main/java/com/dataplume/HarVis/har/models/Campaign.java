package com.dataplume.HarVis.har.models;

import com.dataplume.HarVis.har.models.crawlers.FacebookCrawler;
import com.dataplume.HarVis.har.models.crawlers.TwitterCrawler;
import com.dataplume.HarVis.har.models.crawlers.YoutubeCrawler;


public class Campaign {
    private Search search;
    private int searchRound;
    private EvolveWord evolveWord;

    public Campaign(Search search) {
        this.search = search;
        this.searchRound = 1;
        this.evolveWord = new EvolveWord(search.getSearchKeywords());
    }

    public void startCrawling()
    {
        Crawler crawler = null;
        switch (search.getSocialMediaType())
        {
            case TWITTER:
                crawler = new TwitterCrawler(search);
                break;
            case YOUTUBE:
                crawler = new YoutubeCrawler(search);
                break;
            case FACEBOOK:
                crawler = new FacebookCrawler(search);
                break;
        }
    }
}
