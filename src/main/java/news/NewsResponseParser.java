package news;

import com.google.gson.*;
import exception.ParserException;

import java.util.ArrayList;
import java.util.List;

public class NewsResponseParser {
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
