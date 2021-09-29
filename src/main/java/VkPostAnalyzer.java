import config.Configuration;
import news.VKNewsClient;
import news.VKNewsManager;

import java.io.IOException;
import java.util.Arrays;

public class VkPostAnalyzer {
    public static void main(String[] args) throws IOException {
        String hashtag = '#' + args[0];
        Integer pastHours = Integer.parseInt(args[1]);
        Configuration configuration = new Configuration();
        int[] res = new VKNewsManager(new VKNewsClient("api.vk.com",
                null,
                true,
                configuration.getAccessKey(),
                configuration.getApiVersion())).getPostsWithHashtagByHourCount(hashtag, pastHours);
        Arrays.stream(res).forEach(System.out::println);
    }
}
