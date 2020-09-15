package com.dataplume.HarVis.har.models.crawlers.youtubecrawler;

import com.dataplume.HarVis.har.models.Post;
import com.dataplume.HarVis.har.models.Search;
import com.dataplume.HarVis.har.models.crawlers.YoutubeCrawler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SeleniumYoutubeCrawler extends YoutubeCrawler {

    private static final String CHROME_DRIVER_PATH = "C:\\Users\\Abdalla\\Downloads\\chromedriver.exe";
    private static final String BASE_URL = "https://www.youtube.com/results?search_query=";
    private static ChromeDriver driver;

    static {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        openBrowserIfNotExist();
    }

    private static void openBrowserIfNotExist()
    {
        try
        {
            driver.getTitle();
        }
        catch (Exception e)
        {
            ChromeOptions options = new ChromeOptions();
            //options.addArguments("--headless");
            driver = new ChromeDriver(options);
        }
    }

    public SeleniumYoutubeCrawler(Search search) {
        super(search);
    }

    @Override
    public List<Post> getData() {
        openBrowserIfNotExist();

        String searchString = search.getSearchKeywords().replaceAll(" ", "+");
        String searchURL = BASE_URL + searchString;

        // initialize selenium and webdriver
        if(!searchURL.equalsIgnoreCase(driver.getCurrentUrl()))
            driver.get(searchURL);

        // get list of video results
        List<WebElement> elements = null;
        elements = getEnoughWebElements();
        ArrayList<Post> youTubeVideoDataList = new ArrayList<>();

        // loop throw each video and open it's page to extract data
        for (int i = 0 ;  i < elements.size() ; i++) {
            WebElement webElement = elements.get(i);
            WebElement titleElement = webElement.findElement(By.id("video-title"));
            String title = titleElement.getAttribute("title");
            String href = titleElement.getAttribute("href");
            String publisher = webElement.findElement(By.id("byline-container")).findElement(By.tagName("a")).getText();
            String briefDescription =  webElement.findElement(By.id("description-text")).findElements(By.tagName("span")).stream().map(tag->tag.getText()).collect(Collectors.joining(" "));
            // open new tab for the video's page
            //ArrayList<String> tabs = openNewTab(driver, href);

            // the constructor
            Post video = new Post(
                    title,
                    briefDescription,//getDescription(driver),
                    search.getSocialMediaType(),
                    null,
                    "Jan 1, 1990",//getDate(driver),
                    getVideoId(href),
                    2,//getViewsCount(driver),
                    new ArrayList<>()//getComments(driver),
                    );
            System.out.println(video);
            youTubeVideoDataList.add(video);

            // Close the video's page tab and back to videos list page
            //driver.close();
            //driver.switchTo().window(tabs.get(0)); // switch back to main screen
        }
        return youTubeVideoDataList;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getDate() {
        return null;
    }

    @Override
    public String getPublisher() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public long getViewsCount() {
        return 0;
    }

    @Override
    public long getLikesCount() {
        return 0;
    }

    @Override
    public long getDisLikesCount() {
        return 0;
    }

    private List<WebElement> getEnoughWebElements() {
        int numberOfElementsInPage = 0;
        List<WebElement> elements = new ArrayList<>();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        //TODO: check if old results can be used again
        //TODO: check campaign mode to accept or reject post
        while (numberOfElementsInPage < search.getMaxSearchResults())
        {
            js.executeScript("window.scrollBy(0,700)");
            elements = driver.findElements(By.tagName("ytd-video-renderer"));
            numberOfElementsInPage = elements.size();

            try {
                if (driver.findElement(By.xpath("//*[@id=\"message-button\"]")) != null)
                    break;
            }
            catch (Exception e){
                System.out.println("Exception in finding no more results");
            }
        }
        return elements.stream()
                .limit(search.getMaxSearchResults())
                .collect(Collectors.toList());
    }
    @JsonIgnore
    public String getVideoId(String href)
    {
        if(href == null || href.isEmpty())
            return "";
        return href.substring("https://www.youtube.com/watch?v=".length()).trim();
    }
}
