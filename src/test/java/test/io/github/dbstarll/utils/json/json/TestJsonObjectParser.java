package test.io.github.dbstarll.utils.json.json;

import io.github.dbstarll.utils.json.json.JsonObjectParser;
import io.github.dbstarll.utils.json.test.Model;
import junit.framework.TestCase;
import org.json.JSONObject;
import org.junit.Test;

public class TestJsonObjectParser extends TestCase {
    private Model model1;
    private String jsonObject;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.model1 = new Model(100, "stringValue1", true, 3.14f, new int[]{1, 2, 3, 4, 5});
        this.jsonObject = new JSONObject(model1).toString();
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
        assertEquals(5, json.length());
        assertEquals(100, json.optInt("intValue"));
        assertEquals("stringValue1", json.optString("stringValue"));
        assertEquals(true, json.optBoolean("booleanValue"));
        assertEquals(3.14f, json.optFloat("floatValue"));
        assertEquals("[1,2,3,4,5]", json.optJSONArray("intArray").toString());
    }
}
