package com.dataplume.HarVis.har.repositories;

import com.dataplume.HarVis.har.enums.SocialMediaType;
import com.dataplume.HarVis.har.models.Author;
import com.dataplume.HarVis.har.models.LiteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.dataplume.HarVis.har.models.LiteModel;
import java.util.List;

@Repository
public interface AuthorRepositoryJpa extends JpaRepository<Author, Long> {

    @Query("SELECT new com.dataplume.HarVis.har.models.LiteModel(author.id, author.idOnSite) FROM Author author where author.socialMediaType = :socialMediaType")
    List<LiteModel> getAlreadySavedLiteAuthorsBySocialMediaType(SocialMediaType socialMediaType);


}
