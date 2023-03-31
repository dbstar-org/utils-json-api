package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.utils.json.JsonParseException;
import io.github.dbstarll.utils.json.test.Model;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestJsonNodeParser {
    private ObjectMapper mapper;
    private Model model1;
    private String jsonObject;

    @BeforeEach
    void setUp() throws Exception {
        this.mapper = new ObjectMapper();
        this.model1 = new Model(100, "中文", true, 3.14f, new int[]{1, 2, 3, 4, 5});
        this.jsonObject = mapper.writeValueAsString(model1);
    }

    @AfterEach
    void tearDown() {
        this.mapper = null;
        this.model1 = null;
        this.jsonObject = null;
    }

    @Test
    void parse() throws Exception {
        final JsonNode json = new JsonNodeParser(mapper).parse(jsonObject);
        assertNotNull(json);
        assertEquals(5, json.size());
        assertEquals(100, json.get("intValue").asInt());
        assertEquals("中文", json.get("stringValue").asText());
        assertTrue(json.get("booleanValue").asBoolean());
        assertEquals(3.14, json.get("floatValue").asDouble());
        assertEquals("[1,2,3,4,5]", json.get("intArray").toString());
    }


    @Test
    void failed() {
        final Exception e = assertThrowsExactly(JsonParseException.class, () -> new JsonNodeParser(mapper).parse("abc"));
        assertNotNull(e.getCause());
        assertEquals(com.fasterxml.jackson.core.JsonParseException.class, e.getCause().getClass());
        assertNotNull(e.getCause().getMessage());
    }
}
