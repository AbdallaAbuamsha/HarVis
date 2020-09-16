package com.dataplume.HarVis.har.models;

import com.dataplume.HarVis.har.models.crawlers.youtubecrawler.SeleniumYoutubeCrawler;
import com.dataplume.HarVis.utils.exceptionshandlers.TextCleaning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class Campaign {

    Logger logger = LoggerFactory.getLogger(Campaign.class);


    private static final int COUNT_OF_EVOLVED_WORDS = 5;
    private Search search;
    private int searchRound;
    private String[][] evolvedWords;

    public Campaign(Search search) {
        this.search = search;
        this.searchRound = 1;
        this.evolvedWords = new String[search.getMaxEvolveDepth()][COUNT_OF_EVOLVED_WORDS];
    }

    public List<Post> startCrawling()
    {
        Crawler crawler = null;
        switch (search.getSocialMediaType())
        {
            case TWITTER:
                //crawler = new TwitterCrawler(search);
                break;
            case YOUTUBE:
                crawler = new SeleniumYoutubeCrawler(search);
                break;
            case FACEBOOK:
                //crawler = new FacebookCrawler(search);
                break;
        }
        if(crawler == null)
            throw new NullPointerException();
        List<Post> postsList = crawler.getData();
        filterData(postsList);
        evolve(crawler, postsList);
        //TODO: save the data

        return postsList;
    }

    private List<Post> evolve(Crawler crawler, List<Post> postsList) {
        //TODO: check if evolving will depend on round posts or all posts
        List<Post> roundPostsList = null;
        String originalKeywordSearch = search.getSearchKeywords();
        for(searchRound = 0 ; searchRound  < search.getMaxEvolveDepth(); searchRound++) {
            evolvedWords[searchRound] = (searchRound == 0)?
                    getMostFrequentWords(postsList)
                    :getMostFrequentWords(roundPostsList);

            roundPostsList = new ArrayList<>();
            for(int i = 0 ; i < COUNT_OF_EVOLVED_WORDS ; i++)
            {
                String newKeywordSearch = search.getSearchKeywords() +" "+ evolvedWords[searchRound][i];
                search.setSearchKeywords(newKeywordSearch);
                List<Post> searchResult = crawler.getData();
                filterData(searchResult);
                search.setSearchKeywords(originalKeywordSearch);
                roundPostsList.addAll(searchResult);
            }
            postsList.addAll(roundPostsList);
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
                originalBufferWriter.write(search.getSearchKeywords()+" "+ title+"\n");/*TEST*/
                originalBufferWriter.flush();/*TEST*/
                title = TextCleaning.removeStopWords(title);
                title = TextCleaning.removeNoneLetters(title);
                title = TextCleaning.removeOneLetterWords(title);
                post.setTitle(title);
                filteredBufferWriter.write(search.getSearchKeywords()+" "+ title+"\n");/*TEST*/
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

    private String[] getMostFrequentWords(List<Post> postList) {
        // get set of posts
        List<String> titles = postList
                .stream()
                .map(p -> Arrays.stream(p.getTitle()
                        .split(" "))
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Map<String, Long> map = titles
                .stream()
                .collect(Collectors.groupingBy(t -> t, Collectors.counting()));

        List<String> list = map.entrySet().stream()
                .filter(m-> isNewWordToEvolve(m.getKey()))
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(COUNT_OF_EVOLVED_WORDS)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        for (String t : list)
            logger.info(t);
        return list.stream().toArray(String[]::new);
    }

    private boolean isNewWordToEvolve(String word)
    {
        for (String[] roundArray: this.evolvedWords) {
            for (String evolvedWord:roundArray) {
                if (word.equalsIgnoreCase(evolvedWord) || search.getSearchKeywords().toLowerCase().contains(word))
                    return false;
            }
        }
        return true;
    }
}
