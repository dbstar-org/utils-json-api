package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.utils.json.JsonParseException;
import io.github.dbstarll.utils.net.api.index.IndexBaseHttpClientResponseHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.io.IOException;

class JsonNodeIndexResponseHandler extends IndexBaseHttpClientResponseHandler<String, JsonNode, JsonNodeIndex> {
    private final ObjectMapper mapper;

    JsonNodeIndexResponseHandler(final HttpClientResponseHandler<String> stringResponseHandler,
                                 final ObjectMapper mapper) {
        super(stringResponseHandler);
        this.mapper = mapper;
    }

    @Override
    protected JsonNodeIndex handleContent(final ContentType contentType, final String content,
                                          final boolean endOfStream) throws IOException {
        if (StringUtils.isBlank(content)) {
            return new JsonNodeIndex(null, -1);
        }
        try (JsonParser parser = mapper.createParser(content)) {
            return new JsonNodeIndex(mapper.readTree(parser), (int) parser.currentLocation().getCharOffset());
        } catch (Exception e) {
            if (endOfStream) {
                throw new JsonParseException(e);
            } else {
                return null;
            }
        }
    }
}
