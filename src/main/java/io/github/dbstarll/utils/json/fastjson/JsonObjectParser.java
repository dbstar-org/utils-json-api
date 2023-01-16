package io.github.dbstarll.utils.json.fastjson;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.github.dbstarll.utils.json.JsonParser;

public class JsonObjectParser implements JsonParser<JSONObject> {
    @Override
    public JSONObject parse(final String str) {
        return JSON.parseObject(str);
    }
}
