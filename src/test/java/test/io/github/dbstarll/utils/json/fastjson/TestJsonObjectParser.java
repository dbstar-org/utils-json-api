package test.io.github.dbstarll.utils.json.fastjson;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.github.dbstarll.utils.json.fastjson.JsonObjectParser;
import io.github.dbstarll.utils.json.test.Model;
import junit.framework.TestCase;
import org.junit.Test;

public class TestJsonObjectParser extends TestCase {
    private Model model1;
    private String jsonObject;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.model1 = new Model(100, "stringValue1", true, 3.14f, new int[]{1, 2, 3, 4, 5});
        this.jsonObject = JSON.toJSONString(model1);
    }

    @Override
    protected void tearDown() throws Exception {
        this.model1 = null;
        this.jsonObject = null;
        super.tearDown();
    }

    @Test
    public void testParse() {
        final JSONObject json = new JsonObjectParser().parse(jsonObject);
        assertNotNull(json);
        assertEquals(5, json.size());
        assertEquals(100, json.getIntValue("intValue"));
        assertEquals("stringValue1", json.getString("stringValue"));
        assertEquals(true, json.getBooleanValue("booleanValue"));
        assertEquals(3.14f, json.getFloatValue("floatValue"));
        assertEquals("[1,2,3,4,5]", json.getJSONArray("intArray").toString());
    }
}
