package io.github.dbstarll.utils.json.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.github.dbstarll.utils.json.JsonParser;

public class JsonArrayParser implements JsonParser<JSONArray> {
    @Override
    public JSONArray parse(String str) {
        return JSON.parseArray(str);
    }
}
