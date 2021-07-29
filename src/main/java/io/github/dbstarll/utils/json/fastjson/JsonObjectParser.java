package io.github.dbstarll.utils.json.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.dbstarll.utils.json.JsonParser;

public class JsonObjectParser implements JsonParser<JSONObject> {
    @Override
    public JSONObject parse(String str) {
        return JSON.parseObject(str);
    }
}
