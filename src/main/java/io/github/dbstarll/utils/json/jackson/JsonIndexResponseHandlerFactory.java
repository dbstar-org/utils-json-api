package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.utils.http.client.response.AbstractResponseHandlerFactory;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

class JsonIndexResponseHandlerFactory extends AbstractResponseHandlerFactory {
    JsonIndexResponseHandlerFactory(final HttpClientResponseHandler<String> stringResponseHandler,
                                    final ObjectMapper mapper) {
        addResponseHandler(JsonNodeIndex.class, new JsonNodeIndexResponseHandler(stringResponseHandler, mapper));
    }
}
