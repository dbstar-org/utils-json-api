package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.github.dbstarll.utils.json.JsonParseException;
import io.github.dbstarll.utils.json.JsonParser;

import static org.apache.commons.lang3.Validate.notNull;

public class JsonArrayParser implements JsonParser<ArrayNode> {
    private final ObjectMapper mapper;

    /**
     * 构建JsonArrayParser.
     *
     * @param mapper ObjectMapper
     */
    public JsonArrayParser(final ObjectMapper mapper) {
        this.mapper = notNull(mapper, "mapper is null");
    }

    @Override
    public ArrayNode parse(final String str) throws JsonParseException {
        try {
            return (ArrayNode) mapper.readTree(str);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }
}
