package news;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class VKNewsManagerTest {
    private VKNewsManager manager;

    @Mock
    private VKNewsClient client;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        manager = new VKNewsManager(client);
        List<Pair<String, Long>> posts = getPosts();

        when(client.getPostsWithHashtagInTimeSegmentCount(anyString(), anyLong(), anyLong()))
                .thenAnswer(invocation -> {
                    String hashtag = invocation.getArgumentAt(0, String.class);
                    Long startTime = invocation.getArgumentAt(1, Long.class);
                    Long endTime = invocation.getArgumentAt(2, Long.class);

                    return (int) posts.stream()
                            .filter(i -> (i.getRight() >= startTime && i.getRight() < endTime))
                            .filter(i -> i.getLeft().contains(hashtag))
                            .count();
                });

    }

    @Test
    public void pastThreeHoursTest() {
        int[] res = manager.getPostsWithHashtagByHourCount("#kek", 3);
        assertEquals(List.of(0, 1, 0), getListFromPrimitive(res));
    }

    @Test
    public void fullDayTest() {
        int[] res = manager.getPostsWithHashtagByHourCount("kek", 24);
        assertEquals(List.of(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1, 0),
                getListFromPrimitive(res));
    }

    private List<Integer> getListFromPrimitive(int[] res) {
        List<Integer> list = new ArrayList<>();
        Arrays.stream(res).forEach(list::add);
        return list;
    }

    private List<Pair<String, Long>> getPosts() {
        return List.of(
                Pair.of("yaya kek yaya", LocalDateTime.now().minusHours(24).toEpochSecond(ZoneOffset.UTC)),
                Pair.of("#kek", LocalDateTime.now().minusHours(7).toEpochSecond(ZoneOffset.UTC)),
                Pair.of("kek kuk kik", LocalDateTime.now().minusHours(7).toEpochSecond(ZoneOffset.UTC)),
                Pair.of("#KEK I am superhero", LocalDateTime.now().minusHours(6).toEpochSecond(ZoneOffset.UTC)),
                Pair.of("hashtag in #hashtag middle of the text", LocalDateTime.now().minusHours(5).toEpochSecond(ZoneOffset.UTC)),
                Pair.of("", LocalDateTime.now().minusHours(4).toEpochSecond(ZoneOffset.UTC)),
                Pair.of("I AM NOT RUSSIAN#lol", LocalDateTime.now().minusHours(3).toEpochSecond(ZoneOffset.UTC)),
                Pair.of("London is a capital of Great Britain#GB#kek", LocalDateTime.now().minusHours(2).toEpochSecond(ZoneOffset.UTC)),
                Pair.of("messageWithoutSpaces#lol", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
        );
    }
}
