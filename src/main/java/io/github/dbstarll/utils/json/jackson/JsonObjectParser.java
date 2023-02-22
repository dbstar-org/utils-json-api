package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dbstarll.utils.json.JsonParseException;
import io.github.dbstarll.utils.json.JsonParser;

import static org.apache.commons.lang3.Validate.notNull;

public class JsonObjectParser implements JsonParser<ObjectNode> {
    private final ObjectMapper mapper;

    /**
     * 构建JsonObjectParser.
     *
     * @param mapper ObjectMapper
     */
    public JsonObjectParser(final ObjectMapper mapper) {
        this.mapper = notNull(mapper, "mapper is null");
    }

    @Override
    public ObjectNode parse(final String str) throws JsonParseException {
        try {
            return (ObjectNode) mapper.readTree(str);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }
}
