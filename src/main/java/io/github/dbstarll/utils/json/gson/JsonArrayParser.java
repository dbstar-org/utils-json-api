package io.github.dbstarll.utils.json.gson;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import io.github.dbstarll.utils.json.JsonParser;

import static org.apache.commons.lang3.Validate.notNull;

public class JsonArrayParser implements JsonParser<JsonArray> {
    private final Gson gson;

    /**
     * 构造JsonArrayParser.
     *
     * @param gson gson对象
     */
    public JsonArrayParser(final Gson gson) {
        this.gson = notNull(gson, "gson is null");
    }

    @Override
    public JsonArray parse(final String str) {
        return gson.fromJson(str, JsonArray.class);
    }
}
