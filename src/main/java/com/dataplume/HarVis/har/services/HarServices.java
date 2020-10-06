package com.dataplume.HarVis.har.services;

import com.dataplume.HarVis.har.models.Campaign;
import com.dataplume.HarVis.har.models.Post;
import com.dataplume.HarVis.har.models.Search;
import com.dataplume.HarVis.har.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HarServices {

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private SearchWordRepository searchWordRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CommentRepository commentRepository;
    public List<Post> startCampaign(Search search)
    {
        //TODO: get old results
        Campaign campaign = new Campaign(search);
        List<Post> posts = campaign.startCrawling();
        return posts;

    }
}
