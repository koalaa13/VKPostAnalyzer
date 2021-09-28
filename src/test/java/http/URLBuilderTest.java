package http;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class URLBuilderTest {
    private final URLBuilder builder = new URLBuilder();
    private static final String HOST = "localhost";
    private static final Integer PORT = 228;
    private static final String PATH = "/kek";
    private static final List<Pair<String, String>> PARAMETERS = List.of(
            Pair.of("kek", "kok"),
            Pair.of("kuk", "kik")
    );
    private static final String SCHEMA = "http";

    private String getCorrectParametersRepresentation() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < PARAMETERS.size(); ++i) {
            Pair<String, String> param = PARAMETERS.get(i);
            stringBuilder.append(param.getLeft())
                    .append('=')
                    .append(param.getRight());
            if (i + 1 < PARAMETERS.size()) {
                stringBuilder.append('&');
            }
        }
        return stringBuilder.toString();
    }

    @Test
    public void urlWithAllPartsTest() {
        final String url = builder.buildUrl(HOST, PORT, PATH, PARAMETERS);
        final String correctUrl = SCHEMA + "://" + HOST + ':' + PORT + PATH + '?' + getCorrectParametersRepresentation();
        assertEquals(correctUrl, url);
    }

    @Test
    public void urlWithoutParametersTest() {
        final String url = builder.buildUrl(HOST, PORT, PATH, null);
        final String correctUrl = SCHEMA + "://" + HOST + ':' + PORT + PATH;
        assertEquals(correctUrl, url);
    }

    @Test
    public void urlWithoutPortTest() {
        final String url = builder.buildUrl(HOST, null, PATH, PARAMETERS);
        final String correctUrl = SCHEMA + "://" + HOST + PATH + '?' + getCorrectParametersRepresentation();
        assertEquals(correctUrl, url);
    }

    @Test
    public void urlWithoutPathTest() {
        final String url = builder.buildUrl(HOST, PORT, null, PARAMETERS);
        final String correctUrl = SCHEMA + "://" + HOST + ':' + PORT + '?' + getCorrectParametersRepresentation();
        assertEquals(correctUrl, url);
    }

    @Test
    public void urlWithoutHostTest() {
        final String url = builder.buildUrl(null, PORT, PATH, PARAMETERS);
        final String correctUrl = SCHEMA + ":" + PATH + '?' + getCorrectParametersRepresentation();
        assertEquals(correctUrl, url);
    }

    @Test
    public void everythingNullTest() {
        assertThrows(RuntimeException.class, () -> builder.buildUrl(null, null, null, null));
    }
}
