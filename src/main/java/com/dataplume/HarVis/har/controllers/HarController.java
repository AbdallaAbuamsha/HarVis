package com.dataplume.HarVis.har.controllers;

import com.dataplume.HarVis.har.models.Post;
import com.dataplume.HarVis.har.models.Search;
import com.dataplume.HarVis.har.services.HarServices;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/har")
public class HarController extends RuntimeException{

    private final HarServices harServices;

    public HarController(@Qualifier("HarServicesSpark") HarServices harServices) {
        this.harServices = harServices;
    }

    @PostMapping
    public ResponseEntity<List<Post>> startCampaign(@Valid @RequestBody Search search)
    {
        //TODO: validate inputs for bad search keywords
        return new ResponseEntity<>(harServices.startCampaign(search), HttpStatus.OK);
    }
}
