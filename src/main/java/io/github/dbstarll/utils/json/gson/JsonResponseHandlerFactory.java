package io.github.dbstarll.utils.json.gson;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.dbstarll.utils.http.client.response.AbstractResponseHandlerFactory;
import io.github.dbstarll.utils.json.JsonResponseHandler;

public class JsonResponseHandlerFactory extends AbstractResponseHandlerFactory {
  public JsonResponseHandlerFactory(Gson gson) {
    addResponseHandler(JsonObject.class, JsonResponseHandler.create(new JsonObjectParser(gson)));
    addResponseHandler(JsonArray.class, JsonResponseHandler.create(new JsonArrayParser(gson)));
  }
}
