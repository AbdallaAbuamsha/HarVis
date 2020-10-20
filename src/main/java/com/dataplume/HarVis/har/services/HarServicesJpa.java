package com.dataplume.HarVis.har.services;

import com.dataplume.HarVis.auth.models.User;
import com.dataplume.HarVis.auth.security.services.UserDetailsImpl;
import com.dataplume.HarVis.har.models.*;
import com.dataplume.HarVis.har.repositories.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
@Qualifier("HarServicesJpa")
public class HarServicesJpa extends HarServices {

    private final SearchRepository searchRepository;

    private final SearchWordRepository searchWordRepository;

    private final AuthorRepositoryJpa authorRepositoryJpa;

    private final PostRepositoryJpa postRepositoryJpa;

    private final CommentRepository commentRepository;


    public HarServicesJpa(SearchRepository searchRepository, SearchWordRepository searchWordRepository, AuthorRepositoryJpa authorRepositoryJpa, PostRepositoryJpa postRepositoryJpa, CommentRepository commentRepository) {
        this.searchRepository = searchRepository;
        this.searchWordRepository = searchWordRepository;
        this.authorRepositoryJpa = authorRepositoryJpa;
        this.postRepositoryJpa = postRepositoryJpa;
        this.commentRepository = commentRepository;
    }

    @Override
    protected void saveSearch(Search search) {
        // Save search
        search = searchRepository.save(search);
    }

    @Override
    protected void saveSearchWords(List<SearchWord> searchWords) {
        // Save SearchWords
        searchWordRepository.saveAll(searchWords);

    }

    @Override
    protected List<LiteModel> saveAuthors(List<Author> authors, Search search) {
        // Save Author

        // Save Authors - get already saved authors
        List<LiteModel> authorLiteList = authorRepositoryJpa.getAlreadySavedLiteAuthorsBySocialMediaType(search.getSocialMediaTypeEnum());
        // Remove repeated author results;
        authors = authors.stream()
                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparing(Author::getIdOnSite))),
                        ArrayList::new));
        // Remove Already Saved Authors
        authors.removeIf(a -> authorLiteList.stream().anyMatch(l -> l.getLabel().equals(a.getIdOnSite())));
        // Save New Authors
        authors = authorRepositoryJpa.saveAll(authors);
        authors.forEach(a -> authorLiteList.add(new LiteModel(a.getId(), a.getIdOnSite())));
        return authorLiteList;
    }

    @Override
    protected List<Post> savePosts(List<Post> posts, List<LiteModel> authors) {
        posts.forEach(p -> p.getAuthor().setId( authors.stream()
                .filter(lite -> lite.getLabel().equals(p.getAuthor().getIdOnSite())).findAny().stream().findFirst().orElse(null).getId()));
        return postRepositoryJpa.saveAll(posts);
    }

}
