package news;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class NewsManager {
    private final NewsClient client;

    public NewsManager(NewsClient client) {
        this.client = client;
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
            res[i] = client.getPostsWithHashtagInTimeSegmentCount(hashtag, startTime, startTime + secondsInHour);
            startTime += secondsInHour;
        }
        return res;
    }
}
