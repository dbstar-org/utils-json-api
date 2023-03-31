package io.github.dbstarll.utils.json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.dbstarll.utils.http.client.request.RelativeUriResolver;
import io.github.dbstarll.utils.json.JsonApiClientTestCase;
import io.github.dbstarll.utils.json.ThrowingBiConsumer;
import io.github.dbstarll.utils.net.api.ApiException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
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
    private Gson gson;
    private String jsonObject;
    private String jsonArray;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.gson = new GsonBuilder().create();
        this.jsonObject = gson.toJson(model1);
        this.jsonArray = gson.toJson(Arrays.asList(model1, model2));
    }

    @AfterEach
    @Override
    protected void tearDown() {
        this.jsonArray = null;
        this.jsonObject = null;
        this.gson = null;
        super.tearDown();
    }

    private void useApi(final ThrowingBiConsumer<MockWebServer, MyApiClient> consumer,
                        final ThrowingConsumer<MockWebServer> customizer) throws Throwable {
        useClient((s, c) -> consumer.accept(s, new MyApiClient(c, s.url("/").toString(), gson)), customizer);
    }


    @Test
    void object() throws Throwable {
        useApi((s, c) -> {
            final JsonObject json = c.object();
            assertNotNull(json);
            assertEquals(5, json.size());
            assertEquals(100, json.get("intValue").getAsInt());
            assertEquals("中文", json.get("stringValue").getAsString());
            assertTrue(json.get("booleanValue").getAsBoolean());
            assertEquals(3.14f, json.get("floatValue").getAsFloat());
            assertEquals("[1,2,3,4,5]", json.get("intArray").getAsJsonArray().toString());
        }, s -> s.enqueue(new MockResponse().setBody(jsonObject).setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)));
    }

    @Test
    void array() throws Throwable {
        useApi((s, c) -> {
            final JsonArray array = c.array();
            assertNotNull(array);
            assertEquals(2, array.size());

            final JsonObject json = array.get(1).getAsJsonObject();
            assertEquals(5, json.size());
            assertEquals(101, json.get("intValue").getAsInt());
            assertEquals("stringValue2", json.get("stringValue").getAsString());
            assertFalse(json.get("booleanValue").getAsBoolean());
            assertEquals(1.41f, json.get("floatValue").getAsFloat());
            assertEquals("[5,4,3,2,1]", json.get("intArray").getAsJsonArray().toString());
        }, s -> s.enqueue(new MockResponse().setBody(jsonArray)));
    }

    private static class MyApiClient extends JsonApiClient {
        public MyApiClient(final HttpClient httpClient, final String uriBase, final Gson gson) {
            super(httpClient, true, gson);
            setUriResolver(new RelativeUriResolver(uriBase));
        }

        public JsonObject object() throws ApiException, IOException {
            return execute(get("/ping.html").build(), JsonObject.class);
        }

        public JsonArray array() throws ApiException, IOException {
            return execute(get("/ping.html").build(), JsonArray.class);
        }
    }
}