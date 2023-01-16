package io.github.dbstarll.utils.json.fastjson;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import io.github.dbstarll.utils.json.JsonParser;

public class JsonArrayParser implements JsonParser<JSONArray> {
    @Override
    public JSONArray parse(final String str) {
        return JSON.parseArray(str);
    }
}
