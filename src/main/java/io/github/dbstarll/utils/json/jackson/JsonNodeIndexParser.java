package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.utils.json.JsonParseException;
import io.github.dbstarll.utils.json.JsonParser;

import static org.apache.commons.lang3.Validate.notNull;

public class JsonNodeIndexParser implements JsonParser<JsonNodeIndex> {
    private final ObjectMapper mapper;

    /**
     * 构建JsonNodeIndexParser.
     *
     * @param mapper ObjectMapper
     */
    public JsonNodeIndexParser(final ObjectMapper mapper) {
        this.mapper = notNull(mapper, "mapper is null");
    }

    @Override
    public JsonNodeIndex parse(final String src) throws JsonParseException {
        try (com.fasterxml.jackson.core.JsonParser parser = mapper.createParser(src)) {
            return new JsonNodeIndex(mapper.readTree(parser), (int) parser.currentLocation().getCharOffset());
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }
}
