package io.github.dbstarll.utils.json.fastjson;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.dbstarll.utils.json.test.Model;
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
        this.jsonArray = JSON.toJSONString(Arrays.asList(model1, model2));
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
        assertEquals(2, array.size());

        final JSONObject json = array.getJSONObject(1);
        assertEquals(5, json.size());
        assertEquals(101, json.getIntValue("intValue"));
        assertEquals("stringValue2", json.getString("stringValue"));
        assertFalse(json.getBooleanValue("booleanValue"));
        assertEquals(1.41f, json.getFloatValue("floatValue"));
        assertEquals("[5,4,3,2,1]", json.getJSONArray("intArray").toString());
    }
}
