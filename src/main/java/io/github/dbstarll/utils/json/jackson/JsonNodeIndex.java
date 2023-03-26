package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.dbstarll.utils.net.api.index.AbstractIndex;

class JsonNodeIndex extends AbstractIndex<JsonNode> {
    JsonNodeIndex(final JsonNode data, final int index) {
        super(data, index);
    }
}
