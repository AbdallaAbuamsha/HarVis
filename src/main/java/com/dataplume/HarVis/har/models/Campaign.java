package com.dataplume.HarVis.har.models;

import com.dataplume.HarVis.har.models.crawlers.FacebookCrawler;
import com.dataplume.HarVis.har.models.crawlers.TwitterCrawler;
import com.dataplume.HarVis.har.models.crawlers.YoutubeCrawler;

import java.util.ArrayList;
import java.util.List;


public class Campaign {
    private Search search;
    private int searchRound;
    private String[][] evolvedWords;

    public Campaign(Search search) {
        this.search = search;
        this.searchRound = 1;
        this.evolvedWords = new String[search.getMaxEvolveDepth()][10];
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
        List<Post> postsList = crawler.getData();
        filterData(postsList);
        List<Post> roundPostsList = null;
        for(searchRound = 0 ; searchRound  < search.getMaxEvolveDepth(); searchRound++) {
            evolvedWords[searchRound] = (searchRound == 0)? getMostFrequentWords(postsList) : getMostFrequentWords(postsList);
            roundPostsList = new ArrayList<>();
            for(int i = 0 ; i < 10 ; i++)
            {
                search.setSearchKeywords(evolvedWords[searchRound][i]);
                roundPostsList.addAll(crawler.getData());
            }
            postsList.addAll(roundPostsList);
        }
    }

    private void filterData(List<Post> postsList) {
    }

    private String[] getMostFrequentWords(List<Post> postList) {
        return null;
    }
}
