import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.newsfeed.responses.SearchResponse;

import java.time.LocalDate;
import java.time.ZoneId;

public class VkPostAnalyzer {
    // TODO move it to prop file
    private static final Integer APP_ID = 7959828;
    private static final String CLIENT_SECRET = "1QvbsldyFke9lQgrvfFI";
    private static final String ACCESS_KEY = "0f33d6800f33d6800f33d680c20f4aa39400f330f33d6806e7e204e643707b94affe6be";
    private static final int secondsInHour = 60 * 60;

    public int[] findCounts(String hashtagToFind, int hoursToFind) throws ClientException, ApiException {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        ServiceActor actor = new ServiceActor(APP_ID, CLIENT_SECRET, ACCESS_KEY);

        LocalDate date = LocalDate.now();
        ZoneId zoneId = ZoneId.systemDefault();
        int startOfTheDay = (int) date.atStartOfDay(zoneId).toEpochSecond();

        int[] postsCounts = new int[hoursToFind];

        SearchResponse response;
        for (int i = 0; i < hoursToFind; ++i) {
            int startTime = startOfTheDay + secondsInHour * i;
            int endTime = startTime + secondsInHour;
            response = vk.newsfeed().search(actor)
                    .q(hashtagToFind)
                    .startTime(startTime)
                    .endTime(endTime)
                    .execute();
            postsCounts[i] = response.getCount();
        }
        return postsCounts;
    }
}
