package io.github.dbstarll.utils.json.fastjson;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.dbstarll.utils.json.JsonParserResponseHandlerFactory;

public class JsonResponseHandlerFactory extends JsonParserResponseHandlerFactory {
    /**
     * 构建JsonResponseHandlerFactory.
     */
    public JsonResponseHandlerFactory() {
        addResponseHandler(JSONObject.class, new JsonObjectParser());
        addResponseHandler(JSONArray.class, new JsonArrayParser());
    }

    /**
     * 构建JsonResponseHandlerFactory.
     *
     * @param alwaysProcessEntity 在status异常时是否仍旧处理Entity
     */
    public JsonResponseHandlerFactory(final boolean alwaysProcessEntity) {
        addResponseHandler(JSONObject.class, new JsonObjectParser(), alwaysProcessEntity);
        addResponseHandler(JSONArray.class, new JsonArrayParser(), alwaysProcessEntity);
    }
}
