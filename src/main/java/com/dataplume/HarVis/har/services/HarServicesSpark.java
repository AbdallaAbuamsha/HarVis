package com.dataplume.HarVis.har.services;

import com.dataplume.HarVis.auth.models.User;
import com.dataplume.HarVis.auth.security.services.UserDetailsImpl;
import com.dataplume.HarVis.har.models.*;
import com.dataplume.HarVis.har.repositories.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("HarServicesSpark")
public class HarServicesSpark extends HarServices {

    private final SearchRepository searchRepository;

    private final SearchWordRepository searchWordRepository;

    private final AuthorRepositorySpark authorRepositorySpark;

    private final PostRepositorySpark postRepositorySpark;

    private final CommentRepository commentRepository;

    private final SparkSession spark;

    public HarServicesSpark(SearchRepository searchRepository, SearchWordRepository searchWordRepository, AuthorRepositorySpark authorRepositorySpark, PostRepositorySpark postRepositorySpark, CommentRepository commentRepository, SparkSession spark) {
        this.searchRepository = searchRepository;
        this.searchWordRepository = searchWordRepository;
        this.authorRepositorySpark = authorRepositorySpark;
        this.postRepositorySpark = postRepositorySpark;
        this.commentRepository = commentRepository;
        this.spark = spark;
    }


    @Override
    protected void saveSearch(Search search) {
        // Assign search to current user
        UserDetailsImpl userDetails = (UserDetailsImpl)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        search.setUser(new User(userDetails.getId()));
        // Save search
        search = searchRepository.save(search);
    }

    @Override
    protected void saveSearchWords(List<SearchWord> searchWords) {
        searchWordRepository.saveAll(searchWords);
    }

    @Override
    protected List<LiteModel> saveAuthors(List<Author> authors, Search search) {
        // Save Author
        Encoder<Author> authorEncoder = Encoders.bean(Author.class);
        Dataset<Author> authorsDS = spark.createDataset(authors, authorEncoder);

        // Save Authors - get already saved authors
        Dataset idAndIdOnSite = authorRepositorySpark
                .getAlreadySavedLiteAuthorsBySocialMediaType(search.getSocialMediaType())
                .withColumnRenamed("idOnSite", "_idOnSite");
        // Remove repeated author results;
        authorsDS = authorsDS.dropDuplicates();

        // Remove Already Saved Authors
        Dataset temp = authorsDS
                .drop(authorsDS.col("id"))
                .join(idAndIdOnSite
                        , authorsDS.col("idOnSite").equalTo(idAndIdOnSite.col("_idOnSite"))
                        , "left");
        temp = temp.drop("_idOnSite");
        authorsDS = temp.where(temp.col("id").isNull()).as(authorEncoder);

        // Save New Authors
        authorRepositorySpark.saveDataSet(authorsDS);
        return null;
    }

    @Override
    protected List<Post> savePosts(List<Post> posts, List<LiteModel> authors) {

        Dataset idAndIdOnSite = authorRepositorySpark.getAlreadySavedLiteAuthorsBySocialMediaType(posts.get(0).getSocialMediaType())
                .withColumnRenamed("id", "author_id")
                .withColumnRenamed("idOnSite", "authorIdOnSite");

        Encoder<Post> postEncoder = Encoders.bean(Post.class);
        Dataset postsDS = spark.createDataset(posts, postEncoder);
        postsDS = postsDS.join(idAndIdOnSite, postsDS.col("author").getField("idOnSite").equalTo(idAndIdOnSite.col("authorIdOnSite")))
                .drop(postsDS.col("author"))
                .drop(idAndIdOnSite.col("authorIdOnSite"));
        postsDS = postsDS
                .withColumn("searchWord_id", postsDS.col("searchWord").getField("sId"))
                .drop(postsDS.col("searchWord"));

        postRepositorySpark.saveAll(postsDS);
        return posts;
    }
}
