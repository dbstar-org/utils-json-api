package io.github.dbstarll.utils.json.json;

import io.github.dbstarll.utils.json.test.Model;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestJsonArrayParser {
    private Model model1;
    private Model model2;
    private String jsonArray;

    @BeforeEach
    void setUp() {
        this.model1 = new Model(100, "stringValue1", true, 3.14f, new int[]{1, 2, 3, 4, 5});
        this.model2 = new Model(101, "stringValue2", false, 1.41f, new int[]{5, 4, 3, 2, 1});
        this.jsonArray = new JSONArray(Arrays.asList(model1, model2)).toString();
    }

    @AfterEach
    void tearDown() {
        this.model1 = null;
        this.model2 = null;
        this.jsonArray = null;
    }

    @Test
    void testParse() {
        final JSONArray array = new JsonArrayParser().parse(jsonArray);
        assertNotNull(array);
        assertEquals(2, array.length());

        final JSONObject json = array.getJSONObject(1);
        assertEquals(5, json.length());
        assertEquals(101, json.optInt("intValue"));
        assertEquals("stringValue2", json.optString("stringValue"));
        assertFalse(json.optBoolean("booleanValue"));
        assertEquals(1.41f, json.optFloat("floatValue"));
        assertEquals("[5,4,3,2,1]", json.optJSONArray("intArray").toString());
    }
}
