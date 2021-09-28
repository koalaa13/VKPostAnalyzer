package news;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import exception.ParserException;

public class NewsResponseParser {
    private static final String COUNT_JSON_NAME = "total_count";
    private static final String RESPONSE_JSON_NAME = "response";

    public int getCountFromResponse(String response) throws ParserException {
        try {
            JsonObject wholeResponseAsJson = (JsonObject) JsonParser.parseString(response);
            JsonObject responseField = wholeResponseAsJson.getAsJsonObject(RESPONSE_JSON_NAME);
            return responseField
                    .get(COUNT_JSON_NAME)
                    .getAsInt();
        } catch (JsonSyntaxException e) {
            throw new ParserException("Can't get field 'response' from response", e);
        }
    }
}
