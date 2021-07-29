package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.utils.json.JsonParseException;
import io.github.dbstarll.utils.json.JsonParser;

import java.io.IOException;

import static org.apache.commons.lang3.Validate.notNull;

public class JsonNodeParser implements JsonParser<JsonNode> {
    private final ObjectMapper mapper;

    public JsonNodeParser(ObjectMapper mapper) {
        this.mapper = notNull(mapper, "mapper is null");
    }

    @Override
    public JsonNode parse(String str) throws JsonParseException {
        try {
            return mapper.readTree(str);
        } catch (IOException e) {
            throw new JsonParseException(e);
        }
    }
}
