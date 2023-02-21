package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dbstarll.utils.json.JsonParserResponseHandlerFactory;

public class JsonResponseHandlerFactory extends JsonParserResponseHandlerFactory {
    /**
     * 创建JsonResponseHandlerFactory.
     *
     * @param mapper ObjectMapper
     */
    public JsonResponseHandlerFactory(final ObjectMapper mapper) {
        addResponseHandler(JsonNode.class, new JsonNodeParser(mapper));
        addResponseHandler(ObjectNode.class, new JsonObjectParser(mapper));
        addResponseHandler(ArrayNode.class, new JsonArrayParser(mapper));
    }

    /**
     * 创建JsonResponseHandlerFactory.
     *
     * @param mapper              ObjectMapper
     * @param alwaysProcessEntity 在status异常时是否仍旧处理Entity
     */
    public JsonResponseHandlerFactory(final ObjectMapper mapper, final boolean alwaysProcessEntity) {
        addResponseHandler(JsonNode.class, new JsonNodeParser(mapper), alwaysProcessEntity);
        addResponseHandler(ObjectNode.class, new JsonObjectParser(mapper), alwaysProcessEntity);
        addResponseHandler(ArrayNode.class, new JsonArrayParser(mapper), alwaysProcessEntity);
    }
}
