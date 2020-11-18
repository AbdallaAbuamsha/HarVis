package com.dataplume.HarVis.har.models;

import com.dataplume.HarVis.har.crawlers.twittercrawler.TwitterSnscrapeCrawler;
import com.dataplume.HarVis.har.enums.SocialMediaType;
import com.dataplume.HarVis.har.crawlers.youtubecrawler.SeleniumLightYoutubeCrawler;
import com.dataplume.HarVis.utils.TextProcessing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Campaign {

    Logger logger = LoggerFactory.getLogger(Campaign.class);

    private static final int COUNT_OF_EVOLVED_WORDS = 3;
    private Search search;
    private int searchRound;


    private List<SearchWord> evolvedSearchWords;
    private List<Post> postsList;
    private List<Author> authorsList;
    private List<Comment> commentsList;

    public Campaign(Search search) {
        this.search = search;
        evolvedSearchWords = new ArrayList<>();
        postsList = new ArrayList<>();
        authorsList = new ArrayList<>();
        commentsList = new ArrayList<>();
    }

    public void startCrawling()
    {
        Crawler crawler = createCrawler(search.getSocialMediaTypeEnum());

        SearchWord startSearchWords = new SearchWord(search.getSearchKeywords(), false, search, 0);

        evolvedSearchWords.add(startSearchWords);

        crawler.getData(startSearchWords);

        List<Post> temp = crawler.getPosts();

        filterData(temp);

        appendNewDataFromCrawler(crawler);

        evolve(crawler);
    }

    private void appendNewDataFromCrawler(Crawler crawler) {
        postsList.addAll(crawler.getPosts());
        authorsList.addAll(crawler.getAuthors());
        commentsList.addAll(crawler.getComments());
    }

    private List<Post> evolve(Crawler crawler) {
        List<SearchWord> lastRoundEvolvedSearchWords = null;
        for(searchRound = 0 ; searchRound  < search.getMaxEvolveDepth(); searchRound++) {
            List<String> mostFrequentWords = getMostFrequentWords(
                    postsList.stream()
                            .map(p -> p.getTitle())
                            .collect(Collectors.toList()));

            lastRoundEvolvedSearchWords = mostFrequentWords.stream()
                    .map(w -> new SearchWord(w, true, search, searchRound+1))
                    .collect(Collectors.toList());

            for(int i = 0 ; i < COUNT_OF_EVOLVED_WORDS ; i++)
            {
                crawler.getData(lastRoundEvolvedSearchWords.get(i));
                List<Post> searchResultedPosts = crawler.getPosts();

                filterData(searchResultedPosts);

                appendNewDataFromCrawler(crawler);
            }
            evolvedSearchWords.addAll(lastRoundEvolvedSearchWords);
            if(postsList.size() >= search.getMaxTotalResults())
                break;
        }
        return postsList;
    }

    private void filterData(List<Post> postsList) {
        //filter titles
        //TODO: remove all files code after testing ends.
        try {/*TEST*/
            File original = new ClassPathResource("original.txt").getFile();/*TEST*/
            File filtered = new ClassPathResource("filtered.txt").getFile();/*TEST*/
            if(!original.exists()) original.createNewFile();/*TEST*/
            if(!filtered.exists()) filtered.createNewFile();/*TEST*/

            FileWriter originalWriter = new FileWriter(original.getName(),true);/*TEST*/
            FileWriter filteredWriter = new FileWriter(filtered.getName(),true);/*TEST*/

            BufferedWriter originalBufferWriter = new BufferedWriter(originalWriter);/*TEST*/
            BufferedWriter filteredBufferWriter = new BufferedWriter(filteredWriter);/*TEST*/

            for (Post post: postsList) {
                String title = post.getTitle();
                originalBufferWriter.write(post.getSearchWord().getFullSearchWords()+" "+ title+"\n");/*TEST*/
                originalBufferWriter.flush();/*TEST*/
                title = TextProcessing.removeStopWords(title);
                title = TextProcessing.removeNoneLetters(title);
                title = TextProcessing.removeOneLetterWords(title);
                post.setTitle(title);
                filteredBufferWriter.write(post.getSearchWord().getFullSearchWords()+" "+ title+"\n");/*TEST*/
                filteredBufferWriter.flush();/*TEST*/
            }

            originalBufferWriter.close();/*TEST*/
            filteredBufferWriter.close();/*TEST*/

            originalWriter.close();/*TEST*/
            filteredWriter.close();/*TEST*/
            System.out.println("Done");/*TEST*/
        } catch(IOException e){/*TEST*/
            e.printStackTrace();/*TEST*/
        }/*TEST*/
    }

    private List<String> getMostFrequentWords(List<String> stirngList) {
        // get set of posts
        List<String> titles = TextProcessing.getWordsListFromStringList(stirngList);

        Map<String, Long> map = TextProcessing.getWordsCount(titles);

        map.keySet().removeAll(evolvedSearchWords.stream().map(w -> w.getWord()).collect(Collectors.toList()));
        map.keySet().removeAll(Arrays.asList(search.getSearchKeywords().toLowerCase().split(" ")));

        List<String> list = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(COUNT_OF_EVOLVED_WORDS)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return list;
    }

    private Crawler createCrawler(SocialMediaType socialMediaType) {
        SearchWord searchWord = new SearchWord(search.getSearchKeywords(), false, search, 0);
        switch (socialMediaType)
        {
            case TWITTER:
                return new TwitterSnscrapeCrawler();
            case YOUTUBE:
                return new SeleniumLightYoutubeCrawler();
            case FACEBOOK:
                //return new FacebookCrawler();
            default:
                throw new NullPointerException();
        }
    }

    public List<Post> getPostsList() {
        return postsList;
    }

    public List<Author> getAuthorsList() {
        return authorsList;
    }

    public List<Comment> getCommentsList() {
        return commentsList;
    }

    public List<SearchWord> getEvolvedSearchWords() {
        return evolvedSearchWords;
    }
}
