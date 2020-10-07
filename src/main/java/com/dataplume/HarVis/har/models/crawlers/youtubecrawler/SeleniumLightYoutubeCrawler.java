package com.dataplume.HarVis.har.models.crawlers.youtubecrawler;

import com.dataplume.HarVis.har.enums.AuthorType;
import com.dataplume.HarVis.har.models.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SeleniumLightYoutubeCrawler extends Crawler {

    Logger logger = LoggerFactory.getLogger(SeleniumLightYoutubeCrawler.class);

    private static final String CHROME_DRIVER_PATH = "/home/abdalla/Work/DataPlume/HarVis/Silinum/chromedriver_linux64/chromedriver";
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
            driver.manage().window().maximize();
        }
    }

    @Override
    public void getData() {
        openBrowserIfNotExist();

        String searchString = searchWord.getFullSearchWords().replaceAll(" ", "+");
        String searchURL = BASE_URL + searchString;

        // initialize selenium and webdriver
        if(!searchURL.equalsIgnoreCase(driver.getCurrentUrl()))
            driver.get(searchURL);

        // get list of video results
        List<WebElement> elements = getEnoughWebElements();

        // loop throw each video and open it's page to extract data
        for (int i = 0 ;  i < elements.size() ; i++) {
            WebElement webElement = elements.get(i);
            String title = getTitle(webElement);
            String id = getId(webElement);
            Author author = getAuthor(webElement);
            String briefDescription =  getDescription(webElement);
            String date = getDate(webElement);
            long viewsCount = getViewsCount(webElement);
            Post video = new Post(
                    title, briefDescription, searchWord.getSearch().getSocialMediaType(), author, date, id, viewsCount, searchWord);
            System.out.println(video);
            postsList.add(video);
            authorsList.add(author);
            // no comments
        }
        //return youTubeVideoDataList;
    }


    @Override
    public String getTitle(Object o) {
        try {
            WebElement webElement = (WebElement) o;
            WebElement titleElement = webElement.findElement(By.id("video-title"));
            return titleElement.getAttribute("title");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            return "";
        }
    }

    @Override
    public String getDescription(Object o) {
        try {
            WebElement webElement = (WebElement) o;
            return webElement.findElement(By.id("description-text"))
                    .findElements(By.tagName("span"))
                    .stream()
                    .filter(s -> !s.getText().isBlank())
                    .map(tag->tag.getText())
                    .collect(Collectors.joining(" "));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        }
    }

    @Override
    public String getDate(Object o) {
        try {
            WebElement webElement = (WebElement) o;
            WebElement metaDataLine = webElement.findElement(By.id("metadata-line"));
            return metaDataLine.findElements(By.tagName("span")).get(1).getText();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        }
    }

    @Override
    public Author getAuthor(Object o) {
        try {
            WebElement webElement = (WebElement) o;
            WebElement authorElement = webElement.findElement(By.id("channel-name")).findElement(By.tagName("a"));
            String name = authorElement.getAttribute("textContent") ;
            String href = authorElement.getAttribute("href");
            String channelId = href.substring(href.lastIndexOf('/') + 1);
            return new Author(name, channelId, searchWord.getSearch().getSocialMediaType(), AuthorType.PUBLISHER);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public String getId(Object o) {
        try {
            WebElement webElement = (WebElement) o;
            String href = webElement.findElement(By.id("video-title")).getAttribute("href");
            return href.substring("https://www.youtube.com/watch?v=".length()).trim();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        }
    }

    @Override
    public long getViewsCount(Object o) {
        // Views Count: view-count style-scope yt-view-count-renderer
        try {
            WebElement webElement = (WebElement) o;
            WebElement metaDataLine = webElement.findElement(By.id("metadata-line"));
            String spanText = metaDataLine.findElements(By.tagName("span")).get(0).getText();
            String textViewWords = spanText
                    .replaceAll(",", "")
                    .replaceAll("views", "")
                    .replaceAll("view", "");
            textViewWords = (textViewWords.contains("."))?
                textViewWords.replace("K", "00").replace("M", "00000").replace(".", "")
                :textViewWords.replace("K", "000").replace("M", "000000");


            return Long.parseLong(textViewWords.trim());
        } catch (Exception e) {
             logger.info(e.getMessage());
             return -1;
        }

    }

    @Override
    public long getLikesCount(Object o) {
        return -1;
    }

    @Override
    public long getDisLikesCount(Object o) {
        return -1;
    }

    @Override
    public List<Comment> getPostComments(Object o) {
        return null;
    }


    private List<WebElement> getEnoughWebElements() {
        List<WebElement> elements = new ArrayList<>();
        //TODO: check campaign mode to accept or reject post
        while (elements.size() < searchWord.getSearch().getMaxSearchResults() || searchWord.getSearch().getMaxSearchResults() == -1)
        {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.END);
            elements = driver.findElements(By.tagName("ytd-video-renderer"));

            try {
                if (driver.findElement(By.xpath("//*[@id=\"message-button\"]")) != null)
                    break;
            }
            catch (Exception e){
                System.out.println("Exception in finding no more results");
            }
        }
        return elements.stream()
                .limit((searchWord.getSearch().getMaxSearchResults() == -1)?elements.size():searchWord.getSearch().getMaxSearchResults())
                .collect(Collectors.toList());
    }

}
