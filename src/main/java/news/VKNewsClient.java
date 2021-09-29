package news;

import http.URLBuilder;
import http.URLReader;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class VKNewsClient {
    private final VKNewsResponseParser parser;
    private final URLReader reader;
    private final URLBuilder builder;
    private final String host;
    private final Integer port;
    private final boolean secured;
    private final String accessKey;
    private final String apiVersion;

    public VKNewsClient(String host,
                        Integer port,
                        boolean secured,
                        String accessKey,
                        String apiVersion) {
        this.apiVersion = apiVersion;
        this.secured = secured;
        this.host = host;
        this.port = port;
        this.accessKey = accessKey;
        parser = new VKNewsResponseParser();
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
                Pair.of("v", apiVersion),
                Pair.of("access_token", accessKey),
                Pair.of("count", "0"),
                Pair.of("start_time", startTime.toString()),
                Pair.of("end_time", endTime.toString())
        );
        if (secured) {
            return builder.buildHttpsUrl(host, port, "/method/newsfeed.search", parameters);
        }
        return builder.buildHttpUrl(host, port, "/method/newsfeed.search", parameters);
    }
}
