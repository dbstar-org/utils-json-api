package io.github.dbstarll.utils.json.fastjson;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.dbstarll.utils.http.client.request.RelativeUriResolver;
import io.github.dbstarll.utils.json.JsonApiClientTestCase;
import io.github.dbstarll.utils.json.ThrowingBiConsumer;
import io.github.dbstarll.utils.net.api.ApiException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.hc.client5.http.classic.HttpClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingConsumer;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonApiClientTest extends JsonApiClientTestCase {
    private String jsonObject;
    private String jsonArray;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.jsonObject = JSONObject.toJSONString(model1);
        this.jsonArray = JSONArray.toJSONString(Arrays.asList(model1, model2));
    }

    @AfterEach
    @Override
    protected void tearDown() {
        this.jsonArray = null;
        this.jsonObject = null;
        super.tearDown();
    }

    private void useApi(final ThrowingBiConsumer<MockWebServer, MyApiClient> consumer,
                        final ThrowingConsumer<MockWebServer> customizer) throws Throwable {
        useClient((s, c) -> consumer.accept(s, new MyApiClient(c, s.url("/").toString())), customizer);
    }


    @Test
    void object() throws Throwable {
        useApi((s, c) -> {
            final JSONObject json = c.object();
            assertNotNull(json);
            assertEquals(5, json.size());
            assertEquals(100, json.getIntValue("intValue"));
            assertEquals("stringValue1", json.getString("stringValue"));
            assertTrue(json.getBooleanValue("booleanValue"));
            assertEquals(3.14f, json.getFloatValue("floatValue"));
            assertEquals("[1,2,3,4,5]", json.getJSONArray("intArray").toString());
        }, s -> s.enqueue(new MockResponse().setBody(jsonObject)));
    }

    @Test
    void array() throws Throwable {
        useApi((s, c) -> {
            final JSONArray array = c.array();
            assertNotNull(array);
            assertEquals(2, array.size());

            final JSONObject json = array.getJSONObject(1);
            assertEquals(5, json.size());
            assertEquals(101, json.getIntValue("intValue"));
            assertEquals("stringValue2", json.getString("stringValue"));
            assertFalse(json.getBooleanValue("booleanValue"));
            assertEquals(1.41f, json.getFloatValue("floatValue"));
            assertEquals("[5,4,3,2,1]", json.getJSONArray("intArray").toString());
        }, s -> s.enqueue(new MockResponse().setBody(jsonArray)));
    }

    private static class MyApiClient extends JsonApiClient {
        public MyApiClient(final HttpClient httpClient, final String uriBase) {
            super(httpClient, true);
            setUriResolver(new RelativeUriResolver(uriBase));
        }

        public JSONObject object() throws ApiException, IOException {
            return execute(get("/ping.html").build(), JSONObject.class);
        }

        public JSONArray array() throws ApiException, IOException {
            return execute(get("/ping.html").build(), JSONArray.class);
        }
    }
}