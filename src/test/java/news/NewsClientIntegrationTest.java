package news;

import extension.HostReachableBeforeAllCallback;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(HostReachableBeforeAllCallback.class)
@HostReachableBeforeAllCallback.HostReachable(NewsClientIntegrationTest.HOST)
class NewsClientIntegrationTest {
    public static final String HOST = "api.vk.com";

    @Test
    public void getCountsTest() {
        assertDoesNotThrow(() -> {
            NewsClient client = new NewsClient(HOST, null);
            Long startTime = LocalDateTime.now().minusHours(3).toEpochSecond(ZoneOffset.UTC);
            Long endTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            int foundPosts = client.getPostsWithHashtagInTimeSegmentCount("#эльбрус", startTime, endTime);
            assertTrue(foundPosts >= 0);
        });
    }
}
