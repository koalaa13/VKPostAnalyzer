package http;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import extension.HostReachableBeforeAllCallback;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(HostReachableBeforeAllCallback.class)
@HostReachableBeforeAllCallback.HostReachable(URLReaderTest.HOST)
class URLReaderTest {
    public static final String HOST = "api.vk.com";
    private final URLReader reader = new URLReader();
    private final URLBuilder urlBuilder = new URLBuilder();

    @Test
    public void readTest() {
        assertDoesNotThrow(() -> {
            final String url = urlBuilder.buildUrl(HOST, null, "/method/newsfeed.search",
                    List.of(Pair.of("v", "5.131")));
            String text = reader.readAsText(url);
            assertFalse(text.isEmpty());
        });
    }
}
