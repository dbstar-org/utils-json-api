package io.github.dbstarll.utils.json.json;

import io.github.dbstarll.utils.json.JsonParser;
import org.json.JSONObject;

public class JsonObjectParser implements JsonParser<JSONObject> {
    @Override
    public JSONObject parse(String str) {
        return new JSONObject(str);
    }
}
