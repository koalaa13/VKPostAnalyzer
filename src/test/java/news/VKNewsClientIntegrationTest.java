package news;

import config.Configuration;
import extension.HostReachableBeforeAllCallback;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(HostReachableBeforeAllCallback.class)
@HostReachableBeforeAllCallback.HostReachable(VKNewsClientIntegrationTest.HOST)
class VKNewsClientIntegrationTest {
    public static final String HOST = "api.vk.com";
    private static Configuration configuration;

    @BeforeAll
    public static void setupConfiguration() throws IOException {
        configuration = new Configuration();
    }

    @Test
    public void getCountsTest() {
        assertDoesNotThrow(() -> {
            VKNewsClient client = new VKNewsClient(HOST,
                    null,
                    true,
                    configuration.getAccessKey(),
                    configuration.getApiVersion());
            Long startTime = LocalDateTime.now().minusHours(3).toEpochSecond(ZoneOffset.UTC);
            Long endTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            int foundPosts = client.getPostsWithHashtagInTimeSegmentCount("#эльбрус", startTime, endTime);
            assertTrue(foundPosts >= 0);
        });
    }
}
