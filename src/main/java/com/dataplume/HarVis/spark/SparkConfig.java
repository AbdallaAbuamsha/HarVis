package com.dataplume.HarVis.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;


@Configuration
public class SparkConfig
{
    @Value("${spark.app.name}")
    private String appName;

    @Value("${spark.master}")
    private String masterUri;

    @Bean
    public SparkConf conf() {
        return new SparkConf().setAppName(appName).setMaster(masterUri).set("spark.network.timeout", "180s");
    }

    @Bean
    public JavaSparkContext sc() {
        return new JavaSparkContext(conf());
    }

    @Bean
    @DependsOn({"conf", "sc"})
    public SparkSession spark(SparkConf conf)
    {
        return SparkSession.builder()
                .config(conf)
                .getOrCreate();
    }
}
