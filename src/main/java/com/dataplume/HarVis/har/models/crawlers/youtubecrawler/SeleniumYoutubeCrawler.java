package com.dataplume.HarVis.har.models.crawlers.youtubecrawler;

import com.dataplume.HarVis.har.enums.AuthorType;
import com.dataplume.HarVis.har.enums.SocialMediaType;
import com.dataplume.HarVis.har.models.*;
import com.sun.istack.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class SeleniumYoutubeCrawler extends Crawler {

    Logger logger = LoggerFactory.getLogger(SeleniumYoutubeCrawler.class);

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

            String href = webElement.findElement(By.id("video-title")).getAttribute("href");
            ArrayList<String> tabs = openNewTab(driver, href);
            List<Comment> comments= null;//getComments(null);
            // the constructor
            String title = getTitle(null);
            String description = getDescription(null);
            SocialMediaType socialMediaType = searchWord.getSearch().getSocialMediaTypeEnum();
            Author author = getAuthor(null);
            String date = getDate(null);
            String id = getId(null);
            Long viewsCount = getViewsCount(null);
            Long likesCount = getLikesCount(null);
            Long dislikesCount = getDisLikesCount(null);
            getDisLikesCount(null);
            Post video = new Post(
                title, description, socialMediaType, author, date, id, viewsCount, comments, likesCount, dislikesCount, searchWord);
            System.out.println(video);
            postsList.add(video);
            authorsList.add(author);
            commentsList.addAll(comments);

            // Close the video's page tab and back to videos list page
            driver.close();
            driver.switchTo().window(tabs.get(0)); // switch back to main screen
        }
        //return youTubeVideoDataList;
    }

    @Override
    public String getTitle(Object o) {
        try {
            WebElement titleElement = driver.findElement(By.xpath("//*[@id=\"container\"]/h1/yt-formatted-string"));
            String title = titleElement.getText();//.getAttribute("title");
            return title;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            return driver.getTitle();
        }
    }

    @Override
    public String getDescription(Object o) {
        try {
            WebElement description = driver.findElement(By.id("description"));
            try {
                WebElement showMoreButtonWrapper = driver.findElement(By.className("more-button"));
                showMoreButtonWrapper.click();
            } catch (Exception e) { }
            String descriptionLines = description.findElements(By.tagName("span"))
                    .stream()
                    .filter(s -> !s.getText().isBlank())
                    .map(s -> s.getText())
                    .collect(Collectors.joining("; "));
            return descriptionLines;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            return "";
        }
    }

    @Override
    public String getDate(Object o) {
        try {
            return driver.findElement(By.xpath("//*[@id=\"date\"]/yt-formatted-string")).getText();
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        }
    }

    @Override
    public Author getAuthor(Object o) {
        try {
            return new Author(driver.findElement(By.xpath("//*[@id=\"text\"]/a")).getText(), "", searchWord.getSearch().getSocialMediaTypeEnum(), AuthorType.PUBLISHER);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            try {
                return new Author(driver.findElement(By.tagName("ytd-channel-name")).findElement(By.tagName("a")).getText(), "", searchWord.getSearch().getSocialMediaTypeEnum(), AuthorType.PUBLISHER);
            }
            catch (Exception ee) {
                logger.error(ee.getMessage());
                return null;
            }
        }
    }

    @Override
    public String getId(Object o) {
        try {
            String href = driver.getCurrentUrl();
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
            WebElement viewsCountWrapper = driver.findElement(By.tagName("yt-view-count-renderer"));
            WebElement viewsSpan = viewsCountWrapper.findElement(By.className("view-count"));
            String spanText = viewsSpan.getText();
            return Long. parseLong(spanText
                    .replaceAll(",", "")
                    .replaceAll("views", "")
                    .replaceAll("view", "")
                    .trim());
        } catch (Exception e) {
             logger.info(e.getMessage());
             return -1;
        }

    }

    @Override
    public long getLikesCount(Object o) {
        try {
        WebElement container = driver.findElement(By.xpath("//*[@id=\"top-level-buttons\"]/ytd-toggle-button-renderer[1]/a"));
        long likes =  Long.parseLong(container.findElement(By.tagName("yt-formatted-string"))
                .getAttribute("aria-label")
                .replaceAll(",", "")
                .replaceAll("likes", "")
                .replaceAll("like", "")
                .trim());
        return likes;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Override
    public long getDisLikesCount(Object o) {
        try{
        WebElement container = driver.findElement(By.xpath("//*[@id=\"top-level-buttons\"]/ytd-toggle-button-renderer[2]/a"));
        long dislikes = Long.parseLong(container.findElement(By.tagName("yt-formatted-string"))
                .getAttribute("aria-label")
                .replaceAll(",", "")
                .replaceAll("dislikes", "")
                .replaceAll("dislike", "")
                .trim());
        return dislikes;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Override
    public List<Comment> getPostComments(Object o) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,700)");
            Wait<WebDriver> wait = loadRepetitiveAttempts(driver);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"contents\"]")));
            WebElement commentsWrapper = driver.findElement(By.xpath("//*[@id=\"contents\"]"));
            //TODO: GET AUTHOR NAME!!!!
            List<Comment> comments=new ArrayList<>();
            commentsWrapper
                    .findElements(By.xpath("//*[@id=\"content-text\"]"))
                    .stream()
                    .map(s -> s.getText())
                    .forEach(s -> comments.add(new Comment(s, new Author("", "", searchWord.getSearch().getSocialMediaTypeEnum(), AuthorType.COMMENTATOR), null)));
            return comments;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            return null;
        }
    }

    private List<WebElement> getEnoughWebElements() {
        int numberOfElementsInPage = 0;
        List<WebElement> elements = new ArrayList<>();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        //TODO: check if old results can be used again
        //TODO: check campaign mode to accept or reject post
        while (numberOfElementsInPage < searchWord.getSearch().getMaxSearchResults() || searchWord.getSearch().getMaxSearchResults() == -1)
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
                .limit((searchWord.getSearch().getMaxSearchResults() == -1)?elements.size():searchWord.getSearch().getMaxSearchResults())
                .collect(Collectors.toList());
    }

    @NotNull
    private static ArrayList<String> openNewTab(ChromeDriver driver, String href) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(String.format("window.open('%s','_blank');", href));
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1)); //switches to new tab
        return tabs;
    }

    private static Wait<WebDriver> loadRepetitiveAttempts(ChromeDriver driver) {
        return new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofSeconds(3))
                .ignoring(NoSuchElementException.class);
    }
}
