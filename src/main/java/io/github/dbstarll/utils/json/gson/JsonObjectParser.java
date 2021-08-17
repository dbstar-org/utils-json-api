package io.github.dbstarll.utils.json.gson;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.dbstarll.utils.json.JsonParser;

import static org.apache.commons.lang3.Validate.notNull;

public class JsonObjectParser implements JsonParser<JsonObject> {
    private final Gson gson;

    /**
     * 构建JsonObjectParser.
     *
     * @param gson Gson
     */
    public JsonObjectParser(final Gson gson) {
        this.gson = notNull(gson, "gson is null");
    }

    @Override
    public JsonObject parse(final String str) {
        return gson.fromJson(str, JsonObject.class);
    }
}
