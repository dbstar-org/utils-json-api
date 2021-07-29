package io.github.dbstarll.utils.json.gson;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import io.github.dbstarll.utils.json.JsonParser;

import static org.apache.commons.lang3.Validate.notNull;

public class JsonArrayParser implements JsonParser<JsonArray> {
    private final Gson gson;

    public JsonArrayParser(Gson gson) {
        this.gson = notNull(gson, "gson is null");
    }

    @Override
    public JsonArray parse(String str) {
        return gson.fromJson(str, JsonArray.class);
    }
}
