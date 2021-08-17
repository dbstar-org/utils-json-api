package io.github.dbstarll.utils.json.fastjson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.dbstarll.utils.http.client.response.AbstractResponseHandlerFactory;
import io.github.dbstarll.utils.json.JsonResponseHandler;

public class JsonResponseHandlerFactory extends AbstractResponseHandlerFactory {
    /**
     * 构建JsonResponseHandlerFactory.
     */
    public JsonResponseHandlerFactory() {
        addResponseHandler(JSONObject.class, JsonResponseHandler.create(new JsonObjectParser()));
        addResponseHandler(JSONArray.class, JsonResponseHandler.create(new JsonArrayParser()));
    }
}
