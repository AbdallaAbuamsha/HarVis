package com.dataplume.HarVis.har.repositories;

import com.dataplume.HarVis.har.models.Post;
import org.apache.spark.sql.Dataset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepositoryJpa extends JpaRepository<Post, Long> {

}