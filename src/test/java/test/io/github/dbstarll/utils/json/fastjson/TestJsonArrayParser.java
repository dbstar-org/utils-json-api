package test.io.github.dbstarll.utils.json.fastjson;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.dbstarll.utils.json.fastjson.JsonArrayParser;
import io.github.dbstarll.utils.json.test.Model;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Arrays;

public class TestJsonArrayParser extends TestCase {
    private Model model1;
    private Model model2;
    private String jsonArray;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.model1 = new Model(100, "stringValue1", true, 3.14f, new int[]{1, 2, 3, 4, 5});
        this.model2 = new Model(101, "stringValue2", false, 1.41f, new int[]{5, 4, 3, 2, 1});
        this.jsonArray = JSON.toJSONString(Arrays.asList(model1, model2));
    }

    @Override
    protected void tearDown() throws Exception {
        this.model1 = null;
        this.model2 = null;
        this.jsonArray = null;
        super.tearDown();
    }

    @Test
    public void testParse() {
        final JSONArray array = new JsonArrayParser().parse(jsonArray);
        assertNotNull(array);
        assertEquals(2, array.size());

        final JSONObject json = array.getJSONObject(1);
        assertEquals(5, json.size());
        assertEquals(101, json.getIntValue("intValue"));
        assertEquals("stringValue2", json.getString("stringValue"));
        assertEquals(false, json.getBooleanValue("booleanValue"));
        assertEquals(1.41f, json.getFloatValue("floatValue"));
        assertEquals("[5,4,3,2,1]", json.getJSONArray("intArray").toString());
    }
}
