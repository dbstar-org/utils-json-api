package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.utils.http.client.response.AbstractResponseHandlerFactory;

class JsonIndexResponseHandlerFactory extends AbstractResponseHandlerFactory {
    JsonIndexResponseHandlerFactory(final ObjectMapper mapper) {
        addResponseHandler(JsonNodeIndex.class, new JsonNodeIndexResponseHandler(mapper));
    }
}
