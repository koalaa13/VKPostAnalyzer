package news;

import extension.HostReachableBeforeAllCallback;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(HostReachableBeforeAllCallback.class)
@HostReachableBeforeAllCallback.HostReachable(NewsClientTest.HOST)
class NewsClientTest {
    public static final String HOST = "api.vk.com";

    @Test
    public void getCountsTest() {
        NewsClient client = new NewsClient();

    }
}
