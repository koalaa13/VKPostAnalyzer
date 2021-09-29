package news;

import com.xebialabs.restito.semantics.Call;
import com.xebialabs.restito.server.StubServer;
import org.apache.commons.lang3.tuple.Pair;
import org.glassfish.grizzly.http.Method;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Condition.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VKNewsClientWithStubServerTest {
    private static final int PORT = 32453;
    // accessKey and apiVersion doesn't matter for stub server tests
    private final VKNewsClient client = new VKNewsClient("localhost", PORT, false, "", "");

    private static final long minTime = 7L;
    private static final long maxTime = 2222L;
    private static final List<Pair<String, Long>> posts = List.of(
            Pair.of("#kek", 11L),
            Pair.of("#KEK I am superhero", 11L),
            Pair.of("kek kuk kik", 22L),
            Pair.of("hashtag in #hashtag middle of the text", 33L),
            Pair.of("London is a capital of Great Britain#GB#kek", 1337L),
            Pair.of("I AM NOT RUSSIAN#lol", 228L),
            Pair.of("messageWithoutSpaces#lol", 99L),
            Pair.of("", 11L)
    );

    @Test
    public void getCountOfSimpleHashTagTest() {
        testWithGivenTimesAndHashtag("#kek", minTime, maxTime);
        testWithGivenTimesAndHashtag("#hashtag", minTime, maxTime);
        testWithGivenTimesAndHashtag("lol", minTime, maxTime);
    }

    @Test
    public void getNotAllPostsBecauseOfTimePeriodTest() {
        testWithGivenTimesAndHashtag("#kek", 11L, 11L);
    }

    @Test
    public void getCountsWithoutHashtagTest() {
        testWithGivenTimesAndHashtag("RUSSIAN", minTime, maxTime);
        testWithGivenTimesAndHashtag("hashtag", minTime, maxTime);
        testWithGivenTimesAndHashtag("Britain", minTime, maxTime);
    }

    @Test
    public void getEmptyHashtagCountsTest() {
        testWithGivenTimesAndHashtag("", minTime, maxTime);
    }

    private static class HasParameterPredicate implements Predicate<Call> {

        private final String parameterName;

        private HasParameterPredicate(String parameterName) {
            this.parameterName = parameterName;
        }

        @Override
        public boolean test(Call call) {
            return call.getParameters().containsKey(parameterName);
        }
    }

    private void testWithGivenTimesAndHashtag(String hashtag, Long startTime, Long endTime) {
        withStubServer(s -> {
            int rightAnswer = (int) posts.stream()
                    .filter(i -> (i.getRight() >= startTime && i.getRight() < endTime))
                    .filter(i -> i.getLeft().contains(hashtag))
                    .count();
            whenHttp(s)
                    .match(method(Method.GET),
                            startsWithUri("/method/newsfeed.search"),
                            parameter("start_time", startTime.toString()),
                            parameter("end_time", endTime.toString()),
                            parameter("q", hashtag),
                            custom(new HasParameterPredicate("access_token")),
                            custom(new HasParameterPredicate("v"))
                    )
                    .then(stringContent(buildCorrectResponseWithCount(  rightAnswer)));

            int result = client.getPostsWithHashtagInTimeSegmentCount(hashtag, startTime, endTime);
            assertEquals(rightAnswer, result);
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

    private void withStubServer(Consumer<StubServer> callback) {
        StubServer stubServer = null;
        try {
            stubServer = new StubServer(VKNewsClientWithStubServerTest.PORT).run();
            callback.accept(stubServer);
        } finally {
            if (stubServer != null) {
                stubServer.stop();
            }
        }
    }
}
