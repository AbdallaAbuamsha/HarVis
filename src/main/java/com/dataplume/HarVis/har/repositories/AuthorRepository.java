package com.dataplume.HarVis.har.repositories;

import com.dataplume.HarVis.har.enums.SocialMediaType;
import com.dataplume.HarVis.har.models.Author;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorRepository {

    Logger logger = LoggerFactory.getLogger(AuthorRepository.class);

    @Autowired
    private SparkSession spark;

    private Encoder<Author> authorEncoder;

    public AuthorRepository() {
        authorEncoder = Encoders.bean(Author.class);
    }

    public List<Author> getPublishersBySocialMediaType(SocialMediaType socialMediaType) {
        return null;
    }

    public void save(List<Author> authors) {

        Dataset<Row> authorsDataset = spark.createDataFrame(authors, Author.class);
        authorsDataset.write()
                .format("jdbc")
                .option("url", "jdbc:mysql://localhost:3306/harvis?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC")
                .option("driver", "com.mysql.cj.jdbc.Driver")
                .option("dbtable", "Author")
                .option("user", "root")
                .option("password", "Root31322!")
                .mode("append")
                .save();

    }

    public void saveDataSet(Dataset<Author> authorsDataset) {

        authorsDataset.write()
                .format("jdbc")
                .option("url", "jdbc:mysql://localhost:3306/harvis?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC")
                .option("driver", "com.mysql.cj.jdbc.Driver")
                .option("dbtable", "Author")
                .option("user", "root")
                .option("password", "Root31322!")
                .mode("append")
                .save();

    }

    public List<Author> getAll() {

        return spark
                .read()
                .format("jdbc")
                .option("url", "jdbc:mysql://localhost:3306/harvis?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC")
                .option("driver", "com.mysql.cj.jdbc.Driver")
                .option("dbtable", "Author")
                .option("user", "root")
                .option("password", "Root31322!").load().as(authorEncoder).collectAsList();
    }

    public Dataset getAlreadySavedAuthorsBySocialMediaType(String socialMediaType) {
        Dataset authors = spark
                .read()
                .format("jdbc")
                .option("url", "jdbc:mysql://localhost:3306/harvis?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC")
                .option("driver", "com.mysql.cj.jdbc.Driver")
                .option("dbtable", "Author")
                .option("user", "root")
                .option("password", "Root31322!").load();
        Dataset authorsByWSocialMediaType = authors.select("id", "idOnSite")
                .select("id", "idOnSite")
                .where(authors.col("socialMediaType").equalTo(socialMediaType));
        return authorsByWSocialMediaType;
    }
}
