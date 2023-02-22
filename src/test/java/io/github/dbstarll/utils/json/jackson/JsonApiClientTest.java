package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dbstarll.utils.http.client.request.RelativeUriResolver;
import io.github.dbstarll.utils.json.JsonApiClientTestCase;
import io.github.dbstarll.utils.json.ThrowingBiConsumer;
import io.github.dbstarll.utils.json.test.Model;
import io.github.dbstarll.utils.net.api.ApiException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.http.client.HttpClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingConsumer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonApiClientTest extends JsonApiClientTestCase {
    private ObjectMapper mapper;
    private String jsonObject;
    private String jsonArray;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.mapper = new ObjectMapper();
        this.jsonObject = mapper.writeValueAsString(model1);
        this.jsonArray = mapper.writeValueAsString(Arrays.asList(model1, model2));
    }

    @AfterEach
    @Override
    protected void tearDown() {
        this.jsonArray = null;
        this.jsonObject = null;
        this.mapper = null;
        super.tearDown();
    }

    private void useApi(final ThrowingBiConsumer<MockWebServer, MyApiClient> consumer,
                        final ThrowingConsumer<MockWebServer> customizer) throws Throwable {
        useClient((s, c) -> consumer.accept(s, new MyApiClient(c, s.url("/").toString(), mapper)), customizer);
    }

    @Test
    void object() throws Throwable {
        useApi((s, c) -> {
            final ObjectNode json = c.object();
            assertNotNull(json);
            assertEquals(5, json.size());
            assertEquals(100, json.get("intValue").asInt());
            assertEquals("stringValue1", json.get("stringValue").asText());
            assertTrue(json.get("booleanValue").asBoolean());
            assertEquals(3.14, json.get("floatValue").asDouble());
            assertEquals("[1,2,3,4,5]", json.get("intArray").toString());
        }, s -> s.enqueue(new MockResponse().setBody(jsonObject)));
    }

    @Test
    void model() throws Throwable {
        useApi((s, c) -> {
            final Model model = c.model();
            assertNotNull(model);
            assertEquals(100, model.getIntValue());
            assertEquals("stringValue1", model.getStringValue());
            assertTrue(model.isBooleanValue());
            assertEquals(3.14, model.getFloatValue(), 0.0001);
            assertArrayEquals(new int[]{1, 2, 3, 4, 5}, model.getIntArray());
        }, s -> s.enqueue(new MockResponse().setBody(jsonObject)));
    }

    @Test
    void array() throws Throwable {
        useApi((s, c) -> {
            final ArrayNode array = c.array();
            assertNotNull(array);
            assertEquals(2, array.size());

            final ObjectNode json = (ObjectNode) array.get(1);
            assertEquals(5, json.size());
            assertEquals(101, json.get("intValue").asInt());
            assertEquals("stringValue2", json.get("stringValue").asText());
            assertFalse(json.get("booleanValue").asBoolean());
            assertEquals(1.41, json.get("floatValue").asDouble());
            assertEquals("[5,4,3,2,1]", json.get("intArray").toString());
        }, s -> s.enqueue(new MockResponse().setBody(jsonArray)));
    }

    @Test
    void models() throws Throwable {
        useApi((s, c) -> {
            final List<Model> array = c.models();
            assertNotNull(array);
            assertEquals(2, array.size());

            final Model model = array.get(1);
            assertEquals(101, model.getIntValue());
            assertEquals("stringValue2", model.getStringValue());
            assertFalse(model.isBooleanValue());
            assertEquals(1.41, model.getFloatValue(), 0.0001);
            assertArrayEquals(new int[]{5, 4, 3, 2, 1}, model.getIntArray());
        }, s -> s.enqueue(new MockResponse().setBody(jsonArray)));
    }

    private static class MyApiClient extends JsonApiClient {
        public MyApiClient(final HttpClient httpClient, final String uriBase, final ObjectMapper mapper) {
            super(httpClient, true, mapper);
            setUriResolver(new RelativeUriResolver(uriBase));
        }

        public ObjectNode object() throws ApiException, IOException {
            return execute(get("/ping.html").build(), ObjectNode.class);
        }

        public Model model() throws ApiException, IOException {
            return executeObject(get("/ping.html").build(), Model.class);
        }

        public ArrayNode array() throws ApiException, IOException {
            return execute(get("/ping.html").build(), ArrayNode.class);
        }

        public List<Model> models() throws ApiException, IOException {
            return executeArray(get("/ping.html").build(), Model.class);
        }
    }
}