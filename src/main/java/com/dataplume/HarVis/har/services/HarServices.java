package com.dataplume.HarVis.har.services;

import com.dataplume.HarVis.auth.models.User;
import com.dataplume.HarVis.auth.security.services.UserDetailsImpl;
import com.dataplume.HarVis.har.models.*;
import com.dataplume.HarVis.har.repositories.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SparkSession spark;
    public List<Author> startCampaign(Search search)
    {
        // Assign search to current user
        UserDetailsImpl userDetails = (UserDetailsImpl)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        search.setUser(new User(userDetails.getId()));

        // Start the campaign
        Campaign campaign = new Campaign(search);
        campaign.startCrawling();

        // Save search
        search = searchRepository.save(search);

        // Save SearchWords
        List<SearchWord> searchWords = searchWordRepository.saveAll(campaign.getEvolvedSearchWords());

        // Save Author
        Encoder<Author> authorEncoder = Encoders.bean(Author.class);
        Dataset<Author> authors = spark.createDataset(campaign.getAuthorsList(), authorEncoder);

        // Save Authors - get already saved authors
        Dataset idAndIdOnSite = authorRepository
                .getAlreadySavedAuthorsBySocialMediaType(search.getSocialMediaType())
                .withColumnRenamed("idOnSite", "_idOnSite");
        // Remove repeated author results;
        authors = authors.dropDuplicates();

        // Remove Already Saved Authors
        Dataset temp = authors
                .drop(authors.col("id"))
                .join(idAndIdOnSite
                        , authors.col("idOnSite").equalTo(idAndIdOnSite.col("_idOnSite"))
                        , "left");
        temp = temp.drop("_idOnSite");
        authors = temp.where(temp.col("id").isNull()).as(authorEncoder);

        // Save New Authors
        authorRepository.saveDataSet(authors);

        //Done: Save Posts TASK
        //Done: Save Posts - Get all authors
        idAndIdOnSite = authorRepository.getAlreadySavedAuthorsBySocialMediaType(search.getSocialMediaType())
                            .withColumnRenamed("id", "author_id")
                            .withColumnRenamed("idOnSite", "authorIdOnSite");
        //Done: Save Posts - add IDs to post author to create foreign key
        List<Post> postsList = campaign.getPostsList();
        postsList.forEach(post ->
                post.setSearchWord(searchWords.stream()
                        .filter(searchWord -> searchWord.getWord().equals(post.getSearchWord().getWord()))
                        .findFirst().get()));
        Encoder<Post> postEncoder = Encoders.bean(Post.class);
        Dataset posts = spark.createDataset(postsList, postEncoder);
        posts = posts.join(idAndIdOnSite, posts.col("author").getField("idOnSite").equalTo(idAndIdOnSite.col("authorIdOnSite")))
                .drop(posts.col("author"))
                .drop(idAndIdOnSite.col("authorIdOnSite"));
        posts = posts
                .withColumn("searchWord_id", posts.col("searchWord").getField("sId"))
                .drop(posts.col("searchWord"));

        postRepository.saveAll(posts);
        List<Author> storedAuthors = authorRepository.getAll();
        return storedAuthors;

    }
}
