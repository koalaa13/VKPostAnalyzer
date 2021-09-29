package news;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import exception.ParserException;

public class VKNewsResponseParser {
    private static final String COUNT_JSON_NAME = "total_count";
    private static final String RESPONSE_JSON_NAME = "response";

    public int getCountFromResponse(String response) {
        try {
            JsonObject wholeResponseAsJson = (JsonObject) JsonParser.parseString(response);
            JsonObject responseField = wholeResponseAsJson.getAsJsonObject(RESPONSE_JSON_NAME);
            return responseField
                    .get(COUNT_JSON_NAME)
                    .getAsInt();
        } catch (JsonSyntaxException | NullPointerException e) {
            throw new ParserException("Parsing error happened while trying to get counts of found posts", e);
        }
    }
}
