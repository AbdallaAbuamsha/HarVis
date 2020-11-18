package com.dataplume.HarVis.har.crawlers.twittercrawler;

import com.dataplume.HarVis.har.models.Author;
import com.dataplume.HarVis.har.models.Comment;
import com.dataplume.HarVis.har.models.Crawler;
import com.dataplume.HarVis.har.models.twitter.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TwitterSnscrapeCrawler extends Crawler {

    static String query = "KFSHRC_HE";
    static String _baseUrl;

    static String _API_AUTHORIZATION_HEADER = "Bearer AAAAAAAAAAAAAAAAAAAAANRILgAAAAAAnNwIzUejRCOuH5E6I8xnZz4puTs=1Zv7ttfk8LF81IUq16cHjhLTvJu4FA33AGWWjCpTnA";
    static int _retries = 3;
    static HashMap<String, String> params;
    static HashMap<String, String> paginationParams;
    static String endpoint = "https://api.twitter.com/2/search/adaptive.json";
    static String _guestToken;
    static String _userAgent;
    static HashMap<String, String> _apiHeaders;
    static CookieStore cookieStore = new BasicCookieStore();
    static HttpClientContext context = HttpClientContext.create();
    static ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);;
    static JsonGenerator generatorEntries;
    static JsonGenerator generator;

    public static void init() {
        _userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945."+(new Random()).nextInt(9999)+" Safari/537."+(new Random()).nextInt(99);
        _baseUrl = "https://twitter.com//search?f=live&lang=en&q="+query+"&src=spelling_expansion_revert_click";
        params = new HashMap<>();
        params.put("include_profile_interstitial_type", "1");
        params.put("include_blocking", "1");
        params.put("include_blocked_by", "1");
        params.put("include_followed_by", "1");
        params.put("include_want_retweets", "1");
        params.put("include_mute_edge", "1");
        params.put("include_can_dm", "1");
        params.put("include_can_media_tag", "1");
        params.put("skip_status", "1");
        params.put("cards_platform", "Web-12");
        params.put("include_cards", "1");
        params.put("include_ext_alt_text", "true");
        params.put("include_quote_count", "true");
        params.put("include_reply_count", "1");
        params.put("tweet_mode", "extended");
        params.put("include_entities", "true");
        params.put("include_user_entities", "true");
        params.put("include_ext_media_color", "true");
        params.put("include_ext_media_availability", "true");
        params.put("send_error_codes", "true");
        params.put("simple_quoted_tweets", "true");
        params.put("q", query);
        params.put("tweet_search_mode", "live");
        params.put("count", "100");
        params.put("query_source", "spelling_expansion_revert_click");
        params.put("pc", "1");
        params.put("spelling_corrections", "1");
        params.put("ext", "ext=mediaStats%2ChighlightedLabel");

        paginationParams = new HashMap<>(params);
        paginationParams.put("cursor", null);

        _apiHeaders = new HashMap<>();
        _apiHeaders.put("User-Agent", _userAgent);
        _apiHeaders.put("Authorization", _API_AUTHORIZATION_HEADER);
        _apiHeaders.put("Referer", _baseUrl);

        context.setCookieStore(cookieStore);

    }

    @Override
    protected void getData() {
        init();
        get_items();
        // Remove save functiones from this file
        // implement inhereted methods
        // return lists
    }

    private static List<String> get_items() {
        return _iter_api_data(endpoint, params, paginationParams, null);
    }

    private static List<String> _iter_api_data(String endpoint, Map params, Map paginationParams, String cursor) {
        Map reqParams;
        boolean stopOnEmptyResponse = false;

        if (cursor == null)
            reqParams = params;
        else {
            reqParams = new HashMap(paginationParams);
            reqParams.put("cursor", cursor);
        }

        while(true) {
            // get the data
            JsonNode jsonResponse = _get_api_data(endpoint, reqParams);

            // if no more data break the loop
            if(jsonResponse == null)
                break;
            JsonNode instructions = null;
            if(jsonResponse.hasNonNull("timeline") && jsonResponse.get("timeline").hasNonNull("instructions"))
                instructions = jsonResponse.get("timeline").get("instructions");
            else
                break;

            // parse and save json response
            parseAndSaveOjbect(jsonResponse);

            // update cursor te get new elements
            JsonNode entries = null;
            String newCursor = "";
            for (JsonNode instruction : instructions) {
                if (instruction.has("addEntries"))
                    entries = instruction.get("addEntries").get("entries");
                else if (instruction.has("replaceEntry"))
                    entries = instruction.get("replaceEntry").get("entry");
                else
                    continue;
                for (JsonNode entry : entries) {
                    boolean hastEntryIdEqualsSqCursorBottom = entry.hasNonNull("entryId") && entry.get("entryId").textValue().equals("sq-cursor-bottom");
                    boolean entryIdEqualsSqCursorBottom = entry.textValue()!= null && entry.textValue().equals("sq-cursor-bottom");
                    boolean hastEntryIdStartsWithCursorBottom_ = entry.hasNonNull("entryId") && entry.get("entryId").textValue().startsWith("cursor-bottom-");
                    boolean entryIdStartsWithCursorBottom_ = entry.textValue() != null && entry.textValue().startsWith("cursor-bottom-");
                    JsonNode cursorNode = null;
                    if ((hastEntryIdEqualsSqCursorBottom || hastEntryIdStartsWithCursorBottom_)||(entryIdEqualsSqCursorBottom || entryIdStartsWithCursorBottom_)) {
                        if(hastEntryIdEqualsSqCursorBottom || hastEntryIdStartsWithCursorBottom_) {
                            newCursor = entry.get("content").get("operation").get("cursor").get("value").textValue();
                            cursorNode = entry.get("content").get("operation").get("cursor");
                        }
                        else if (entryIdEqualsSqCursorBottom || entryIdStartsWithCursorBottom_) {
                            newCursor = entries.get("content").get("operation").get("cursor").get("value").textValue();
                            cursorNode = entries.get("content").get("operation").get("cursor");
                        }

                        if (cursorNode.has("stopOnEmptyResponse") || (cursorNode.textValue() != null && cursorNode.textValue().equals("stopOnEmptyResponse")))
                            stopOnEmptyResponse = entry.get("content").get("operation").get("cursor").get("stopOnEmptyResponse").asBoolean();
                    }
                }
            }

            if (newCursor.isEmpty() || newCursor.equals(cursor) || (stopOnEmptyResponse && getTweetsCount(instructions) == 0))
                break;

            cursor = newCursor;
            reqParams = new HashMap(paginationParams);
            reqParams.put("cursor",cursor);
        }
        return null;
    }

    private static Map parseAndSaveOjbect(JsonNode jsonResponse) {
        JsonNode entries = null;
        JsonNode instructions = jsonResponse.get("timeline").get("instructions");
        JsonNode tweet = null;
        for (JsonNode instruction : instructions) {
            if (instruction.has("addEntries"))
                entries = instruction.get("addEntries").get("entries");
            else if (instruction.has("replaceEntry"))
                entries = instruction.get("replaceEntry").get("entry");
            else
                continue;

            for (JsonNode entry : entries) {
                if (entry.has("entryId") && (entry.get("entryId").textValue().startsWith("sq-I-t-") || entry.get("entryId").textValue().startsWith("tweet-")))
                {
                    if (entry.get("content").get("item").get("content").has("tweet" ))
                    {
                        if (entry.get("content").get("item").get("content").get("tweet").has("promotedMetadata")) // Promoted tweet aka ads
                            continue;

                        if (!jsonResponse.get("globalObjects").get("tweets").has(entry.get("content").get("item").get("content").get("tweet").get("id").textValue()))
                        {
                            System.out.println("Skipping tweet {"+entry.get("content").get("item").get("content").get("tweet").get("id").textValue() +"}which is not in globalObjects");
                            continue;
                        }
                        tweet = jsonResponse.get("globalObjects").get("tweets").get(entry.get("content").get("item").get("content").get("tweet").get("id").textValue());
                    }
                    else if (entry.get("content").get("item").get("content").has("tombstone") && entry.get("content").get("item").get("content").get("tombstone").has("tweet"))
                    {
                        if (!jsonResponse.get("globalObjects").get("tweets").has(entry.get("content").get("item").get("content").get("tombstone").get("tweet").get("id").textValue()))
                        {
                            System.out.println("Skipping tweet "+entry.get("content").get("item").get("content").get("tombstone").get("tweet").get("id")+"which is not in globalObjects");
                            continue;
                        }
                        tweet = jsonResponse.get("globalObjects").get("tweets").get(entry.get("content").get("item").get("content").get("tombstone").get("tweet").get("id").textValue());
                    }
                    else
                        System.out.println("Unable to handle entry "+entry.get("entryId"));

                    Map tweetMap = _tweet_to_tweet(tweet, jsonResponse);
                    try {
                        mapper.writeValue(generator, tweetMap);
                        mapper.writeValue(generator, ",");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    private static Map _tweet_to_tweet(JsonNode tweet, JsonNode jsonResponse)
    {
        Map<String, Object> kwargs = new HashMap<>();
        kwargs.put("id", tweet.has("id")?tweet.get("id") : tweet.get("id_str"));
        kwargs.put("content", tweet.get("full_text"));
        kwargs.put("renderedContent", _render_text_with_urls(tweet.get("full_text").textValue(), tweet.get("entities").get("urls")));
        kwargs.put("username", jsonResponse.get("globalObjects").get("users").get(tweet.get("user_id_str").textValue()).get("screen_name"));
        kwargs.put("user", _user_to_user(jsonResponse.get("globalObjects").get("users").get(tweet.get("user_id_str").textValue())));
        kwargs.put("date", tweet.get("created_at"));
        ArrayNode outlinks_urls = (tweet.get("entities").has("urls"))?(ArrayNode) tweet.get("entities").get("urls") : new ArrayNode(null);
        kwargs.put("outlinks", StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(outlinks_urls.elements(), Spliterator.ORDERED),false)
                .map(u -> u.get("expanded_url"))
                .collect(Collectors.toCollection(ArrayList::new)));
        ArrayNode tcooutlinks_urls = (tweet.get("entities").has("urls"))?(ArrayNode) tweet.get("entities").get("urls") : new ArrayNode(null);
        kwargs.put("tcooutlinks", StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(tcooutlinks_urls.elements(), Spliterator.ORDERED),false)
                .map(u -> u.get("url"))
                .collect(Collectors.toCollection(ArrayList::new)));
        kwargs.put("url", "https://twitter.com/%s/status/%s".formatted(kwargs.get("username").toString(), kwargs.get("id").toString()));
        kwargs.put("replyCount", tweet.get("reply_count"));
        kwargs.put("retweetCount", tweet.get("retweet_count"));
        kwargs.put("likeCount", tweet.get("favorite_count"));
        kwargs.put("quoteCount", tweet.get("quote_count"));
        kwargs.put("conversationId", tweet.has("conversation_id")? tweet.get("conversation_id") : tweet.get("conversation_id_str"));
        kwargs.put("lang", tweet.get("lang"));
        kwargs.put("source", tweet.get("source"));


        if (tweet.has("extended_entities") && tweet.get("extended_entities").has("media")) {
            ArrayList<Medium> media = new ArrayList<>();
            for (JsonNode medium : tweet.get("extended_entities").get("media")) {
                if (medium.get("type").textValue().equals("photo")) {
                    if (!medium.get("media_url_https").textValue().contains(".")) {
                        System.out.println("Skipping malformed medium URL on tweet %s: %s contains no dot".formatted(kwargs.get("id"), medium.get("media_url_https").textValue()));
                        continue;
                    }
                    String baseUrl = medium.get("media_url_https").textValue().substring(0, medium.get("media_url_https").textValue().lastIndexOf('.'));
                    String format = medium.get("media_url_https").textValue().substring(medium.get("media_url_https").textValue().lastIndexOf('.') + 1);

                    if (!format.equals("jpg") && !format.equals("png"))//(format not in ('jpg', 'png'))
                    {
                        System.out.println("Skipping photo with unknown format on tweet %s: %s".formatted(kwargs.get("id"), format));
                        continue;
                    }
                    media.add(new Photo("%s?format=%s&name=small".formatted(baseUrl, format), "%s?format=%s&name=large".formatted(baseUrl, format)));
                } else if (medium.get("type").textValue().equals("video") || medium.get("type").equals("animated_gif")) {
                    List<VideoVariant> variants = new ArrayList<>();
                    for (JsonNode variant : medium.get("video_info").get("variants")) {
                        variants.add(new VideoVariant(variant.get("content_type").textValue(), variant.get("url").textValue(), (variant.has("bitrate")) ? variant.get("bitrate").asInt() : null));
                    }
                    String thumbnailUrl = medium.get("media_url_https").textValue();
                    Medium cls = null;
                    if (medium.get("type").equals("video")) {
                        float duration = medium.get("video_info").get("duration_millis").asInt() / 1000;
                        cls = new Video(thumbnailUrl, variants, duration);
                    } else if (medium.get("type").equals("animated_gif")) {
                        cls = new Gif(thumbnailUrl, variants);
                    }
                    if (cls != null)    // not from original code :3
                        media.add(cls);
                }
            }
            if (media != null) {
                kwargs.put("media", media);
            }
        }
        kwargs.put("retweetedTweet", tweet.has("retweeted_status_id_str")?
                _tweet_to_tweet(jsonResponse.get("globalObjects").get("tweets").get(tweet.get("retweeted_status_id_str").textValue()), jsonResponse)
                : null);
        if (tweet.has("quoted_status_id_str") && jsonResponse.get("globalObjects").get("tweets").has(tweet.get("quoted_status_id_str").textValue()))
            kwargs.put("quotedTweet", _tweet_to_tweet(jsonResponse.get("globalObjects").get("tweets").get(tweet.get("quoted_status_id_str").textValue()), jsonResponse));

        List<User> mentionedUsers = null;
        if (tweet.get("entities").has("user_mentions") && tweet.get("entities").hasNonNull("user_mentions")) {
            mentionedUsers = new ArrayList<>();
            for (JsonNode user : tweet.get("entities").get("user_mentions"))
                mentionedUsers.add( new User( user.get("screen_name").textValue()
                        , user.get("name").textValue()
                        , user.has("id")?
                        String.valueOf(user.get("id").intValue())
                        : user.get("id_str").textValue()));
        }
        kwargs.put("mentionedUsers", mentionedUsers);
        return kwargs;
    }

    private static String _render_text_with_urls(String text, JsonNode urls)
    {
        if (urls != null)
            return text;
        List<String> out = new ArrayList<>();
        out.add(text.substring(0, urls.get(0).get("indices").get(0).textValue().length()));
        for (int i = 0 ; i < urls.size(); i++) {
            JsonNode url = (urls.has(i))?urls.get(i) : null;
            JsonNode nextUrl = (urls.has(1))?urls.get(1): null;
            if (url != null)
                out.add(url.get("display_url").textValue());
            if (nextUrl != null)
                out.add(text.substring(url.get("indices").get(1).asInt() ,nextUrl.get("indices").get(0).asInt()));
        }
        return out.stream().collect(Collectors.joining(""));
    }

    private static Map _user_to_user(JsonNode user)
    {
        Map<String, Object> kwargs = new HashMap();
        kwargs.put("username" , user.get("screen_name"));
        kwargs.put("displayname" , user.get("name"));
        kwargs.put("id" , user.has("id") ? user.get("id") : user.get("id_str"));
        kwargs.put("rawDescription" , user.get("description"));
        JsonNode description = user.get("entities").get("description");
        if (description.has("urls"))
        {
            ArrayNode urls = (ArrayNode) description.get("urls");
            kwargs.put("descriptionUrls", StreamSupport
                    .stream(Spliterators.spliteratorUnknownSize(urls.elements(), Spliterator.ORDERED),false)
                    .map(u -> new DescriptionURL(u.get("display_url").textValue(), u.get("expanded_url").textValue(), u.get("url").textValue(), new int[]{ u.get("indices").get(0).asInt(), u.get("indices").get(1).asInt()}))
                    .collect(Collectors.toCollection(ArrayList::new)));
        }
        else
            kwargs.put("descriptionUrls", null);
        kwargs.put("verified" , user.get("verified"));
        kwargs.put("created" , user.get("created_at"));
        kwargs.put("followersCount" , user.get("followers_count"));
        kwargs.put("friendsCount" , user.get("friends_count"));
        kwargs.put("statusesCount" , user.get("statuses_count"));
        kwargs.put("favouritesCount" , user.get("favourites_count"));
        kwargs.put("listedCount" , user.get("listed_count"));
        kwargs.put("mediaCount" , user.get("media_count"));
        kwargs.put("location" , user.get("location"));
        kwargs.put("protected" , user.get("protected"));
        if (user.get("entities").hasNonNull("url") &&
                user.get("entities").get("url").hasNonNull("urls") &&
                user.get("entities").get("url").get("urls").get(0).hasNonNull("expanded_url"))
            kwargs.put("linkUrl" , user.get("entities").get("url").get("urls").get(0).get("expanded_url"));
        else if (user.hasNonNull("url"))
            kwargs.put("linkUrl" , user.get("url"));
        else
            kwargs.put("linkUrl" , null);

        kwargs.put("linkTcourl" , user.get("url"));
        kwargs.put("profileImageUrl" , user.get("profile_image_url_https"));
        kwargs.put("profileBannerUrl" , user.get("profile_banner_url"));
        return kwargs;
    }

    private static JsonNode _get_api_data(String endpoint, Map params) {
        _ensure_guest_token();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost("api.twitter.com").setPath("/2/search/adaptive.json");
        HttpResponse response = sendGet(builder, params, _apiHeaders, -1, true);
        JsonNode jsonResponse = null;
        try {
            String content = EntityUtils.toString(response.getEntity());
            jsonResponse = mapper.readTree(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }

    private static int getTweetsCount(JsonNode instructions) {
        JsonNode entries;
        int count = 0;
        for (JsonNode instruction : instructions)
        {
            {
                if (instruction.has("addEntries"))
                    entries = instruction.get("addEntries").get("entries");
                else if (instruction.has("replaceEntry"))
                    entries = instruction.get("replaceEntry").get("entry");
                else
                    continue;

                for (JsonNode entry : entries)
                    if(entry.has("entryId") && (entry.get("entryId").textValue().startsWith("sq-I-t-") || entry.get("entryId").textValue().startsWith("tweet-")))
                        count +=1;
            }
        }
        return count;
    }

    static void _ensure_guest_token() {
        if (_guestToken != null && !_guestToken.isEmpty())
            return;
        System.out.println("Retrieving guest token");
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent", _userAgent);
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("twitter.com").setPath("/search")
                .setParameter("f", "live")
                .setParameter("lang", "en")
                .setParameter("q", "KFSHRC_HE")
                .setParameter("src", "spelling_expansion_revert_click");
        HttpResponse resultHttpResponse = sendGet(builder, null, headers, -1, true);
        if (resultHttpResponse == null)
            return;
        String content = null;
        try {
            content = EntityUtils.toString(resultHttpResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String token = getGuestToken(content);
        if(token != null && !token.isBlank())
            _guestToken = token;
        CookieStore cookieStore = context.getCookieStore();
        // get Cookies
        List<Cookie> cookies = cookieStore.getCookies();
        Optional<Cookie> guestTokenCookie = cookies.stream().filter(c -> c.getName().contains("gt")).findFirst();
        if (guestTokenCookie.isPresent())
            _guestToken = guestTokenCookie.get().getValue();
        if (_guestToken != null && !_guestToken.isBlank()) {
            //https://hc.apache.org/httpcomponents-client-ga/tutorial/html/statemgmt.html
            BasicClientCookie cookie = new BasicClientCookie("gt", _guestToken);
            cookie.setDomain(".twitter.com");
            cookie.setPath("/");
            cookie.setSecure(true);
            LocalDateTime dateTime = LocalDateTime.now().plus(Duration.of(30, ChronoUnit.MINUTES));
            Date expiryDate = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
            cookie.setExpiryDate(expiryDate);
            cookieStore.addCookie(cookie);
            _apiHeaders.put("x-guest-token", _guestToken);
            System.out.println("token = " + _guestToken);
            return;
        }
        System.out.println("Unable to find guest token");
    }

    private static String getGuestToken(String content) {
        String pattern = "document\\.cookie = decodeURIComponent\\(\"gt=(\\d+); Max-Age=10800; Domain=\\.twitter\\.com; Path=/; Secure\"\\);";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(content);
        if (m.find())
            return m.group(1);
        else {
            System.out.println(content);
            System.out.println("NO MATCH");
            return "";
        }
    }

    public static HttpResponse sendGet(URIBuilder builder, Map<String, String> params, Map<String, String> headers, int timeout, boolean allowRedirects) {
        HttpGet request = null;
        HttpResponse response = null;
        boolean requestFailed;
        for (int attempt = 0; attempt < _retries; attempt++) {
            requestFailed = false;
            try {

                RequestConfig.Builder rc = RequestConfig.custom();
                if (timeout > 0) {
                    rc.setConnectTimeout(timeout);
                    rc.setSocketTimeout(timeout);
                }
                rc.setRedirectsEnabled(allowRedirects);

                HttpClient client = HttpClientBuilder.create().build();

                if (params != null)
                    for (String param : params.keySet())
                        builder.setParameter(param, params.get(param));

                URI uri = builder.build();

                System.out.println("url = " + uri.toURL().toString());

                request = new HttpGet(uri);

                if (headers != null)
                    for (String header : headers.keySet())
                        request.addHeader(header, headers.get(header));

                request.setConfig(rc.build());

                response = client.execute(request, context);

                if(response.getStatusLine().getStatusCode() == 429)
                {
                    _unset_guest_token();
                    _ensure_guest_token();
                    requestFailed = true;
                }
                if (request.getFirstHeader("content-type")!=null && request.getFirstHeader("content-type").getValue().replace(" ", "").equals("application/json;charset=utf-8"))
                    System.out.println("content type is not JSON");

            } catch (URISyntaxException e) {
                e.printStackTrace();
                requestFailed = true;
            } catch (IOException e) {
                e.printStackTrace();
                requestFailed = true;
            } finally {
                if (requestFailed)
                    try {
                        long sleepTime = 2 * attempt + 1;
                        TimeUnit.SECONDS.sleep(sleepTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                else break;
            }
        }
        return response;
    }

    private static void _unset_guest_token() {
        _guestToken = null;
        cookieStore = new BasicCookieStore();
        context = HttpClientContext.create();
        context.setCookieStore(cookieStore);
        _apiHeaders.remove("x-guest-token");
    }

    @Override
    public String getTitle(Object o) {
        return null;
    }

    @Override
    public String getDescription(Object o) {
        return null;
    }

    @Override
    public String getDate(Object o) {
        return null;
    }

    @Override
    public Author getAuthor(Object o) {
        return null;
    }

    @Override
    public String getId(Object o) {
        return null;
    }

    @Override
    public long getViewsCount(Object o) {
        return 0;
    }

    @Override
    public long getLikesCount(Object o) {
        return 0;
    }

    @Override
    public long getDisLikesCount(Object o) {
        return 0;
    }

    @Override
    public List<Comment> getPostComments(Object o) {
        return null;
    }


}
