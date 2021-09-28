package news;

import exception.ParserException;
import http.URLBuilder;
import http.URLReader;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class NewsClient {
    private final NewsResponseParser parser;
    private final URLReader reader;
    private final URLBuilder builder;
    // TODO move it to prop file
    private final static String HOST = "api.vk.com";
    private final static String ACCESS_KEY = "0f33d6800f33d6800f33d680c20f4aa39400f330f33d6806e7e204e643707b94affe6be";
    private final static String API_VERSION = "5.131";

    public NewsClient() {
        parser = new NewsResponseParser();
        reader = new URLReader();
        builder = new URLBuilder();
    }

    /**
     * @param hashtag   hashtag to find post with
     * @param startTime left bound for creation time of post
     * @param endTime   right bound for creation time of post
     * @return count of found posts with given hashtag created in given hour or 0 if error happened
     */
    public int getPostsWithHashtagInTimeSegmentCount(String hashtag, Long startTime, Long endTime) {
        String response = reader.readAsText(createUrl(hashtag, startTime, endTime));
        try {
            return parser.getCountFromResponse(response);
        } catch (ParserException ignored) {
            return 0;
        }
    }

    /**
     * the same as {@link NewsClient#getPostsWithHashtagInTimeSegmentCount(String, Long, Long)}
     * but get array of posts counts created in previous cntPastHours.
     *
     * @return array with size cntPastHours. Containing counts.
     * For example if time right now is 15:00 and cntPastHours = 3, the first element
     * of array will be equals count of posts with hashtag and created in period [12:00;13:00],
     * the second element in period [13:00;14:00] and e.t.c.
     */
    public int[] getPostsWithHashtagByHourCount(String hashtag, Integer cntPastHours) {
        int[] res = new int[cntPastHours];
        final long secondsInHour = 60 * 60;
        long startTime = LocalDateTime.now().minusHours(cntPastHours).toEpochSecond(ZoneOffset.UTC);
        for (int i = 0; i < cntPastHours; ++i) {
            res[i] = getPostsWithHashtagInTimeSegmentCount(hashtag, startTime, startTime + secondsInHour);
            startTime += secondsInHour;
        }
        return res;
    }

    private String createUrl(String hashtag,
                             Long startTime,
                             Long endTime) {
        List<Pair<String, String>> parameters = List.of(
                Pair.of("q", hashtag),
                Pair.of("v", API_VERSION),
                Pair.of("access_key", ACCESS_KEY),
                Pair.of("count", "0"),
                Pair.of("start_time", startTime.toString()),
                Pair.of("end_time", endTime.toString())
        );
        return builder.buildUrl(HOST, null, "/method/newsfeed.search", parameters);
    }
}
