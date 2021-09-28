package news;

import com.xebialabs.restito.server.StubServer;
import org.glassfish.grizzly.http.Method;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Condition.method;
import static com.xebialabs.restito.semantics.Condition.startsWithUri;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NewsClientWithStubServerTest {
    private static final int PORT = 32453;
    private final NewsClient client = new NewsClient("localhost", PORT);

    // TODO this test doesn't work because idk how to work sith stub server via https
    // TODO but can get JSON response from vk api only via https
    @Test
    public void simpleTest() {
        withStubServer(PORT, s -> {
            whenHttp(s)
                    .match(method(Method.GET), startsWithUri("/method/newsfeed.search"))
                    .then(stringContent(buildCorrectResponseWithCount(33)));

            long startTime = 0;
            long endTime = 0;
            int result = client.getPostsWithHashtagInTimeSegmentCount("#kek", startTime, endTime);
            assertEquals(33, result);
        });
    }

    private String buildCorrectResponseWithCount(int count) {
        return "{\n" +
                "    \"response\": {\n" +
                "        \"count\": " + count + ",\n" +
                "        \"items\": [],\n" +
                "        \"total_count\": " + count + "\n" +
                "    }\n" +
                "}";
    }

    private void withStubServer(int port, Consumer<StubServer> callback) {
        StubServer stubServer = null;
        try {
            stubServer = new StubServer(port).run();
            callback.accept(stubServer);
        } finally {
            if (stubServer != null) {
                stubServer.stop();
            }
        }
    }
}
