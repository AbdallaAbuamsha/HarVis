package com.dataplume.HarVis.har.repositories;

import com.dataplume.HarVis.har.models.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends JpaRepository<Search, Long> {


}
