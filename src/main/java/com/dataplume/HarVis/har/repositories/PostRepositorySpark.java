package com.dataplume.HarVis.har.repositories;

import org.apache.spark.sql.Dataset;
import org.springframework.stereotype.Repository;


@Repository
public class PostRepositorySpark {

    public void saveAll(Dataset postsDataset) {
        postsDataset.write()
                .format("jdbc")
                .option("url", "jdbc:mysql://localhost:3306/harvis?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC")
                .option("driver", "com.mysql.cj.jdbc.Driver")
                .option("dbtable", "Post")
                .option("user", "root")
                .option("password", "Root31322!")
                .mode("append")
                .save();
    }
}