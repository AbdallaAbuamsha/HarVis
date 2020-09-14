package com.dataplume.HarVis.har;

import com.dataplume.HarVis.har.models.Campaign;
import com.dataplume.HarVis.har.models.Search;
import org.springframework.stereotype.Service;

@Service
public class HarServices {

    private void startCampaign(Search search)
    {
        Campaign campaign = new Campaign(search);
    }
}
