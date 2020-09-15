package com.dataplume.HarVis.har.services;

import com.dataplume.HarVis.har.models.Campaign;
import com.dataplume.HarVis.har.models.Post;
import com.dataplume.HarVis.har.models.Search;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HarServices {

    public List<Post> startCampaign(Search search)
    {
        Campaign campaign = new Campaign(search);
        return campaign.startCrawling();
    }
}
