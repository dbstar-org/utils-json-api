package io.github.dbstarll.utils.json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.dbstarll.utils.json.test.Model;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestJsonArrayParser {
    private Gson gson;
    private Model model1;
    private Model model2;
    private String jsonArray;

    @BeforeEach
    void setUp() {
        this.gson = new GsonBuilder().create();
        this.model1 = new Model(100, "stringValue1", true, 3.14f, new int[]{1, 2, 3, 4, 5});
        this.model2 = new Model(101, "stringValue2", false, 1.41f, new int[]{5, 4, 3, 2, 1});
        this.jsonArray = gson.toJson(Arrays.asList(model1, model2));
    }

    @AfterEach
    void tearDown() {
        this.gson = null;
        this.model1 = null;
        this.model2 = null;
        this.jsonArray = null;
    }

    @Test
    void testParse() {
        final JsonArray array = new JsonArrayParser(gson).parse(jsonArray);
        assertNotNull(array);
        assertEquals(2, array.size());

        final JsonObject json = array.get(1).getAsJsonObject();
        assertEquals(5, json.size());
        assertEquals(101, json.get("intValue").getAsInt());
        assertEquals("stringValue2", json.get("stringValue").getAsString());
        assertEquals(false, json.get("booleanValue").getAsBoolean());
        assertEquals(1.41f, json.get("floatValue").getAsFloat());
        assertEquals("[5,4,3,2,1]", json.get("intArray").getAsJsonArray().toString());
    }
}
