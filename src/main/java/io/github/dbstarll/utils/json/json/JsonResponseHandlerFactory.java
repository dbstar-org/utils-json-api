package io.github.dbstarll.utils.json.json;

import io.github.dbstarll.utils.http.client.response.AbstractResponseHandlerFactory;
import io.github.dbstarll.utils.json.JsonResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonResponseHandlerFactory extends AbstractResponseHandlerFactory {
  /**
   * 构建JsonResponseHandlerFactory.
   */
  public JsonResponseHandlerFactory() {
    addResponseHandler(JSONObject.class, JsonResponseHandler.create(new JsonObjectParser()));
    addResponseHandler(JSONArray.class, JsonResponseHandler.create(new JsonArrayParser()));
  }
}
