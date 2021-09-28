package news;

import http.URLBuilder;
import http.URLReader;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class NewsClient {
    private final NewsResponseParser parser;
    private final URLReader reader;
    private final URLBuilder builder;
    // TODO move it to prop file
    private final String host;
    private final Integer port;
    private final static String ACCESS_KEY = "0f33d6800f33d6800f33d680c20f4aa39400f330f33d6806e7e204e643707b94affe6be";
    private final static String API_VERSION = "5.131";

    public NewsClient(String host, Integer port) {
        this.host = host;
        this.port = port;
        parser = new NewsResponseParser();
        reader = new URLReader();
        builder = new URLBuilder();
    }

    /**
     * @param hashtag   hashtag to find post with
     * @param startTime left bound for creation time of post in UNIX TIMESTAMP FORMAT
     * @param endTime   right bound for creation time of post in UNIX TIMESTAMP FORMAT
     * @return count of found posts with given hashtag created in given hour
     */
    public int getPostsWithHashtagInTimeSegmentCount(String hashtag, Long startTime, Long endTime) {
        String response = reader.readAsText(createUrl(hashtag, startTime, endTime));
        return parser.getCountFromResponse(response);
    }

    private String createUrl(String hashtag,
                             Long startTime,
                             Long endTime) {
        List<Pair<String, String>> parameters = List.of(
                Pair.of("q", hashtag),
                Pair.of("v", API_VERSION),
                Pair.of("access_token", ACCESS_KEY),
                Pair.of("count", "0"),
                Pair.of("start_time", startTime.toString()),
                Pair.of("end_time", endTime.toString())
        );
        return builder.buildHttpsUrl(host, port, "/method/newsfeed.search", parameters);
    }
}
