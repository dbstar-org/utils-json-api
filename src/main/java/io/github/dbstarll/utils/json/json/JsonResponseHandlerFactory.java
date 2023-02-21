package io.github.dbstarll.utils.json.json;

import io.github.dbstarll.utils.json.JsonParserResponseHandlerFactory;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonResponseHandlerFactory extends JsonParserResponseHandlerFactory {
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
