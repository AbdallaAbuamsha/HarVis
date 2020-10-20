package com.dataplume.HarVis.har.services;

import com.dataplume.HarVis.har.models.*;

import java.util.List;

public abstract class HarServices {
    public List<Post> startCampaign(Search search)
    {
        // Start the campaign
        Campaign campaign = new Campaign(search);
        campaign.startCrawling();

        saveSearch(search);

        saveSearchWords(campaign.getEvolvedSearchWords());

        List<LiteModel> authors = saveAuthors(campaign.getAuthorsList(), search);

        List<Post> posts = savePosts(campaign.getPostsList(), authors);

        return posts;

    }
    protected abstract void saveSearch(Search search);
    protected abstract void saveSearchWords(List<SearchWord> searchWords);
    protected abstract List<LiteModel> saveAuthors(List<Author> authors, Search search);
    protected abstract List<Post> savePosts(List<Post> posts, List<LiteModel> authors);

}
