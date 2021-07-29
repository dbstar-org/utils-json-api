package test.io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.utils.json.jackson.JsonNodeParser;
import io.github.dbstarll.utils.json.test.Model;
import junit.framework.TestCase;
import org.junit.Test;

public class TestJsonNodeParser extends TestCase {
    private ObjectMapper mapper;
    private Model model1;
    private String jsonObject;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.mapper = new ObjectMapper();
        this.model1 = new Model(100, "stringValue1", true, 3.14f, new int[]{1, 2, 3, 4, 5});
        this.jsonObject = mapper.writeValueAsString(model1);
    }

    @Override
    protected void tearDown() throws Exception {
        this.mapper = null;
        this.model1 = null;
        this.jsonObject = null;
        super.tearDown();
    }

    @Test
    public void testParse() throws Exception {
        final JsonNode json = new JsonNodeParser(mapper).parse(jsonObject);
        assertNotNull(json);
        assertEquals(5, json.size());
        assertEquals(100, json.get("intValue").asInt());
        assertEquals("stringValue1", json.get("stringValue").asText());
        assertEquals(true, json.get("booleanValue").asBoolean());
        assertEquals(3.14, json.get("floatValue").asDouble());
        assertEquals("[1,2,3,4,5]", json.get("intArray").toString());
    }
}
