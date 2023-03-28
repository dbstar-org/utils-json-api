package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.utils.json.JsonParseException;
import io.github.dbstarll.utils.net.api.index.IndexBaseHttpClientResponseHandler;

import java.io.IOException;

class JsonNodeIndexResponseHandler extends IndexBaseHttpClientResponseHandler<JsonNodeIndex> {
    private final ObjectMapper mapper;

    JsonNodeIndexResponseHandler(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    protected JsonNodeIndex handleContent(final String content, final boolean endOfStream) throws IOException {
        try (JsonParser parser = mapper.createParser(content)) {
            return new JsonNodeIndex(mapper.readTree(parser), (int) parser.currentLocation().getCharOffset());
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }
}
