package io.github.dbstarll.utils.json.json;

import io.github.dbstarll.utils.json.JsonParser;
import org.json.JSONArray;

public class JsonArrayParser implements JsonParser<JSONArray> {
    @Override
    public JSONArray parse(String str) {
        return new JSONArray(str);
    }
}
