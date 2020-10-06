package com.dataplume.HarVis.har.repositories;

import com.dataplume.HarVis.har.models.Post;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class PostRepository {

    @Autowired
    JavaSparkContext sc;

    void saveAll(List<Post> posts)
    {

    }
}