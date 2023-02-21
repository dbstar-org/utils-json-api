package io.github.dbstarll.utils.json.gson;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.dbstarll.utils.json.JsonParserResponseHandlerFactory;

public class JsonResponseHandlerFactory extends JsonParserResponseHandlerFactory {
    /**
     * 构建JsonResponseHandlerFactory.
     *
     * @param gson                Gson
     * @param alwaysProcessEntity 在status异常时是否仍旧处理Entity
     */
    public JsonResponseHandlerFactory(final Gson gson, final boolean alwaysProcessEntity) {
        addResponseHandler(JsonObject.class, new JsonObjectParser(gson), alwaysProcessEntity);
        addResponseHandler(JsonArray.class, new JsonArrayParser(gson), alwaysProcessEntity);
    }
}
