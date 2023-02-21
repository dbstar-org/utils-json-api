package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dbstarll.utils.json.test.Model;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestJsonArrayParser {
    private ObjectMapper mapper;
    private Model model1;
    private Model model2;
    private String jsonArray;

    @BeforeEach
    void setUp() throws Exception {
        this.mapper = new ObjectMapper();
        this.model1 = new Model(100, "stringValue1", true, 3.14f, new int[]{1, 2, 3, 4, 5});
        this.model2 = new Model(101, "stringValue2", false, 1.41f, new int[]{5, 4, 3, 2, 1});
        this.jsonArray = mapper.writeValueAsString(Arrays.asList(model1, model2));
    }

    @AfterEach
    void tearDown() {
        this.mapper = null;
        this.model1 = null;
        this.model2 = null;
        this.jsonArray = null;
    }

    @Test
    void testParse() throws Exception {
        final ArrayNode array = new JsonArrayParser(mapper).parse(jsonArray);
        assertNotNull(array);
        assertEquals(2, array.size());

        final ObjectNode json = (ObjectNode) array.get(1);
        assertEquals(5, json.size());
        assertEquals(101, json.get("intValue").asInt());
        assertEquals("stringValue2", json.get("stringValue").asText());
        assertEquals(false, json.get("booleanValue").asBoolean());
        assertEquals(1.41, json.get("floatValue").asDouble());
        assertEquals("[5,4,3,2,1]", json.get("intArray").toString());
    }
}
