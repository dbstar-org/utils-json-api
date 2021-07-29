package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dbstarll.utils.http.client.response.AbstractResponseHandlerFactory;
import io.github.dbstarll.utils.json.JsonResponseHandler;

public class JsonResponseHandlerFactory extends AbstractResponseHandlerFactory {
  /**
   * 创建JsonResponseHandlerFactory.
   *
   * @param mapper ObjectMapper
   */
  public JsonResponseHandlerFactory(ObjectMapper mapper) {
    addResponseHandler(JsonNode.class, JsonResponseHandler.create(new JsonNodeParser(mapper)));
    addResponseHandler(ObjectNode.class, JsonResponseHandler.create(new JsonObjectParser(mapper)));
    addResponseHandler(ArrayNode.class, JsonResponseHandler.create(new JsonArrayParser(mapper)));
  }

  /**
   * 创建JsonResponseHandlerFactory.
   *
   * @param mapper              ObjectMapper
   * @param alwaysProcessEntity 在status异常时是否仍旧处理Entity
   */
  public JsonResponseHandlerFactory(ObjectMapper mapper, boolean alwaysProcessEntity) {
    addResponseHandler(JsonNode.class, JsonResponseHandler.create(new JsonNodeParser(mapper), alwaysProcessEntity));
    addResponseHandler(ObjectNode.class, JsonResponseHandler.create(new JsonObjectParser(mapper), alwaysProcessEntity));
    addResponseHandler(ArrayNode.class, JsonResponseHandler.create(new JsonArrayParser(mapper), alwaysProcessEntity));
  }
}
