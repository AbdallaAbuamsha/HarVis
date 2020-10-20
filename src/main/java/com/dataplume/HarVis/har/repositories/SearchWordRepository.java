package com.dataplume.HarVis.har.repositories;

import com.dataplume.HarVis.har.models.SearchWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchWordRepository extends JpaRepository<SearchWord, Long> {
    List<SearchWord> findBySearchId(Long id);

}
