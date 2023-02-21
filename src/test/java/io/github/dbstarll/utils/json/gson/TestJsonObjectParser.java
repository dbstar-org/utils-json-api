package io.github.dbstarll.utils.json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.dbstarll.utils.json.test.Model;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestJsonObjectParser {
    private Gson gson;
    private Model model1;
    private String jsonObject;

    @BeforeEach
    void setUp() {
        this.gson = new GsonBuilder().create();
        this.model1 = new Model(100, "stringValue1", true, 3.14f, new int[]{1, 2, 3, 4, 5});
        this.jsonObject = gson.toJson(model1);
    }

    @AfterEach
    void tearDown() {
        this.gson = null;
        this.model1 = null;
        this.jsonObject = null;
    }

    @Test
    void testParse() {
        final JsonObject json = new JsonObjectParser(gson).parse(jsonObject);
        assertNotNull(json);
        assertEquals(5, json.size());
        assertEquals(100, json.get("intValue").getAsInt());
        assertEquals("stringValue1", json.get("stringValue").getAsString());
        assertTrue(json.get("booleanValue").getAsBoolean());
        assertEquals(3.14f, json.get("floatValue").getAsFloat());
        assertEquals("[1,2,3,4,5]", json.get("intArray").getAsJsonArray().toString());
    }
}
