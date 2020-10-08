package com.dataplume.HarVis.har.services;

import com.dataplume.HarVis.har.models.Author;
import com.dataplume.HarVis.har.models.Campaign;
import com.dataplume.HarVis.har.models.Search;
import com.dataplume.HarVis.har.repositories.AuthorRepository;
import com.dataplume.HarVis.har.repositories.CommentRepository;
import com.dataplume.HarVis.har.repositories.SearchRepository;
import com.dataplume.HarVis.har.repositories.SearchWordRepository;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class HarServices {

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private SearchWordRepository searchWordRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SparkSession spark;
    public List<Author> startCampaign(Search search)
    {
        Campaign campaign = new Campaign(search);
        campaign.startCrawling();
        //DONE: Save search TASK
        searchRepository.save(search);
        //DONE: Save searchWords TASK
        searchWordRepository.saveAll(campaign.getEvolvedSearchWords());
        Encoder<Author> authorEncoder = Encoders.bean(Author.class);
        Dataset<Author> authors = spark.createDataset(campaign.getAuthorsList(), authorEncoder);
//        authors.show(10);
        //DONE: Save Authors TASK
        //DONE: Save Authors - get already saved authors
        Dataset idAndIdOnSite = authorRepository.getAlreadySavedAuthorsBySocialMediaType(search.getSocialMediaType())
                .withColumnRenamed("idOnSite", "_idOnSite");
        //DONE: Remove repeated author results;
        authors = authors.dropDuplicates();
        //DONE: Remove Already Saved Authors
//        authors.show(10);
        Dataset temp = authors
                .drop(authors.col("id"))
                .join(idAndIdOnSite, authors.col("idOnSite").equalTo(idAndIdOnSite.col("_idOnSite")), "left");

        temp.show();
        temp = temp.drop("_idOnSite");
        temp.show(10);
        authors = temp.where(temp.col("id").isNull()).as(authorEncoder);
//        authors.show(10);
//        authors = temp.drop("_idOnSite").as(authorEncoder);
//        authors.show();
//        authors =authors.filter(authors.col("id").isNull())
//                .as(authorEncoder);
        authors.show(10);
        //DONE: Save Authors - Save New Authors
        authorRepository.saveDataSet(authors);
        //Done: Get All Authors List IDs, and idOnSite to add IDs to Posts
        idAndIdOnSite = authorRepository.getAlreadySavedAuthorsBySocialMediaType(search.getSocialMediaType());
        //TODO: Save Posts TASK
        //TODO: Save Posts - Get all authors
        //TODO: Save Posts - add IDs to post author to create foreign key
        List<Author> storedAuthors = authorRepository.getAll();
        return storedAuthors;

    }
}
