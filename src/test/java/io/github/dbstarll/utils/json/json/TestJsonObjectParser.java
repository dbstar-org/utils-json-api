package io.github.dbstarll.utils.json.json;

import io.github.dbstarll.utils.json.test.Model;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestJsonObjectParser {
    private Model model1;
    private String jsonObject;

    @BeforeEach
    void setUp() {
        this.model1 = new Model(100, "中文", true, 3.14f, new int[]{1, 2, 3, 4, 5});
        this.jsonObject = new JSONObject(model1).toString();
    }

    @AfterEach
    void tearDown() {
        this.model1 = null;
        this.jsonObject = null;
    }

    @Test
    void testParse() {
        final JSONObject json = new JsonObjectParser().parse(jsonObject);
        assertNotNull(json);
        assertEquals(5, json.length());
        assertEquals(100, json.optInt("intValue"));
        assertEquals("中文", json.optString("stringValue"));
        assertTrue(json.optBoolean("booleanValue"));
        assertEquals(3.14f, json.optFloat("floatValue"));
        assertEquals("[1,2,3,4,5]", json.optJSONArray("intArray").toString());
    }
}
