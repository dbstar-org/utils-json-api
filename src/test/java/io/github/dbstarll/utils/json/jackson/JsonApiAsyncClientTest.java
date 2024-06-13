package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dbstarll.utils.http.client.request.RelativeUriResolver;
import io.github.dbstarll.utils.json.JsonApiClientTestCase;
import io.github.dbstarll.utils.json.JsonParseException;
import io.github.dbstarll.utils.json.ThrowingBiConsumer;
import io.github.dbstarll.utils.json.jackson.JsonApiClientTest.ModelParams;
import io.github.dbstarll.utils.json.test.Model;
import io.github.dbstarll.utils.net.api.ApiException;
import io.github.dbstarll.utils.net.api.StreamFutureCallback;
import io.github.dbstarll.utils.net.api.index.EventStream;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.hc.client5.http.async.HttpAsyncClient;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingConsumer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

class JsonApiAsyncClientTest extends JsonApiClientTestCase {
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
        useAsyncClient((s, c) -> consumer.accept(s, new MyApiClient(c, s.url("/").toString(), mapper)), customizer);
    }

    @Test
    void object() throws Throwable {
        useApi((s, c) -> {
            final MyFutureCallback<ObjectNode> callback = new MyFutureCallback<>();
            final ObjectNode json = c.object(callback).get();
            callback.assertResult(json);
            assertNotNull(json);
            assertEquals(5, json.size());
            assertEquals(100, json.get("intValue").asInt());
            assertEquals("中文", json.get("stringValue").asText());
            assertTrue(json.get("booleanValue").asBoolean());
            assertEquals(3.14, json.get("floatValue").asDouble());
            assertEquals("[1,2,3,4,5]", json.get("intArray").toString());
        }, s -> s.enqueue(new MockResponse().setBody(jsonObject)));
    }

    @Test
    void model() throws Throwable {
        useApi((s, c) -> {
            final MyFutureCallback<Model> callback = new MyFutureCallback<>();
            final Model model = c.model(callback).get();
            callback.assertResult(model);
            assertNotNull(model);
            assertEquals(100, model.getIntValue());
            assertEquals("中文", model.getStringValue());
            assertTrue(model.isBooleanValue());
            assertEquals(3.14, model.getFloatValue(), 0.0001);
            assertArrayEquals(new int[]{1, 2, 3, 4, 5}, model.getIntArray());
        }, s -> s.enqueue(new MockResponse().setBody(jsonObject)));
    }

    @Test
    void modelEntity() throws Throwable {
        useApi((s, c) -> {
            final MyFutureCallback<Model> callback = new MyFutureCallback<>();
            final Model model = c.model(new ModelParams("name"), callback).get();
            callback.assertResult(model);
            assertNotNull(model);
            assertEquals(100, model.getIntValue());
            assertEquals("中文", model.getStringValue());
            assertTrue(model.isBooleanValue());
            assertEquals(3.14, model.getFloatValue(), 0.0001);
            assertArrayEquals(new int[]{1, 2, 3, 4, 5}, model.getIntArray());
        }, s -> s.enqueue(new MockResponse().setBody(jsonObject).setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)));
    }

    @Test
    void array() throws Throwable {
        useApi((s, c) -> {
            final MyFutureCallback<ArrayNode> callback = new MyFutureCallback<>();
            final ArrayNode array = c.array(callback).get();
            callback.assertResult(array);
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
            final MyFutureCallback<List<Model>> callback = new MyFutureCallback<>();
            final List<Model> array = c.models(callback).get();
            callback.assertResult(array);
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

    @Test
    void stream() throws Throwable {
        useApi((s, c) -> {
            final MyStreamFutureCallback<Model> callback = new MyStreamFutureCallback<>();
            assertNull(c.streamModel(callback).get());
            assertEquals(2, callback.results.size());

            final Model model = callback.results.get(1);
            assertEquals(101, model.getIntValue());
            assertEquals("stringValue2", model.getStringValue());
            assertFalse(model.isBooleanValue());
            assertEquals(1.41, model.getFloatValue(), 0.0001);
            assertArrayEquals(new int[]{5, 4, 3, 2, 1}, model.getIntArray());
        }, s -> s.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)
                .setBody(jsonObject + "\n" + mapper.writeValueAsString(model2))));
    }

    @Test
    void streamModelNull() throws Throwable {
        useApi((s, c) -> {
            final MyStreamFutureCallback<Model> callback = new MyStreamFutureCallback<>();
            assertNull(c.streamModel(callback).get());
            assertEquals(0, callback.results.size());
        }, s -> s.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)
                .setBody(" \n ")));
    }

    @Test
    void streamNull() throws Throwable {
        useApi((s, c) -> {
            final MyStreamFutureCallback<JsonNode> callback = new MyStreamFutureCallback<>();
            assertNull(c.stream(callback).get());
            assertEquals(0, callback.results.size());
        }, s -> s.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)
                .setBody(" \n ")));
    }

    @Test
    void streamException() throws Throwable {
        useApi((s, c) -> {
            final MyStreamFutureCallback<Model> callback = new MyStreamFutureCallback<>();
            final ExecutionException e = assertThrowsExactly(ExecutionException.class, () -> c.streamModel(callback).get());
            assertNotNull(e.getCause());
            assertSame(JsonParseException.class, e.getCause().getClass());
            assertNotNull(e.getCause().getCause());
            assertSame(JsonEOFException.class, e.getCause().getCause().getClass());
        }, s -> s.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)
                .setBody("{")));
    }

    @Test
    void streamNode() throws Throwable {
        useApi((s, c) -> {
            final MyStreamFutureCallback<JsonNode> callback = new MyStreamFutureCallback<>();
            assertNull(c.stream(callback).get());
            assertEquals(1, callback.results.size());
            assertEquals("{}", callback.results.get(0).toString());
        }, s -> s.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)
                .setBody("{}")));
    }

    @Test
    void streamNodeException() throws Throwable {
        useApi((s, c) -> {
            final MyStreamFutureCallback<JsonNode> callback = new MyStreamFutureCallback<>();
            final ExecutionException e = assertThrowsExactly(ExecutionException.class, () -> c.stream(callback).get());
            assertNotNull(e.getCause());
            assertSame(JsonParseException.class, e.getCause().getClass());
            assertNotNull(e.getCause().getCause());
            assertSame(JsonEOFException.class, e.getCause().getCause().getClass());
        }, s -> s.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)
                .setBody("{")));
    }

    @Test
    void streamEvent() throws Throwable {
        useApi((s, c) -> {
            final MyIgnoreEventStreamFutureCallback<JsonNode> callback = new MyIgnoreEventStreamFutureCallback<>();
            assertNull(c.event(callback).get());
            assertEquals(2, callback.results.size());
            assertEquals("{}", callback.results.get(0).toString());
            assertEquals("[]", callback.results.get(1).toString());
        }, s -> s.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, ContentType.TEXT_EVENT_STREAM)
                .setBody("data: {}\n\ndata: []\n\ndata:  \n\ndata: ignore\n\n  \n\n")));
    }

    @Test
    void streamEventUnsupportedEncoding() throws Throwable {
        useApi((s, c) -> {
            final MyIgnoreEventStreamFutureCallback<JsonNode> callback = new MyIgnoreEventStreamFutureCallback<>();
            final ExecutionException e = assertThrowsExactly(ExecutionException.class, () -> c.event(callback).get());
            assertNotNull(e.getCause());
            assertSame(UnsupportedEncodingException.class, e.getCause().getClass());
            assertEquals("UTF-88", e.getCause().getMessage());
            callback.assertException(e.getCause());
        }, s -> s.enqueue(new MockResponse().setBody("data: {}\n\ndata: []\n\ndata:  \n\ndata: ignore\n\n  \n\n")
                .setHeader(HttpHeaders.CONTENT_TYPE, "text/event-stream; charset=UTF-88")));
    }

    @Test
    void streamEventModel() throws Throwable {
        useApi((s, c) -> {
            final MyEventStreamFutureCallback<Model> callback = new MyEventStreamFutureCallback<>();
            assertNull(c.eventModel(callback).get());
            assertEquals(1, callback.results.size());
            final Model model = callback.results.get(0);
            assertEquals(100, model.getIntValue());
            assertEquals("中文", model.getStringValue());
            assertTrue(model.isBooleanValue());
            assertEquals(3.14, model.getFloatValue(), 0.0001);
            assertArrayEquals(new int[]{1, 2, 3, 4, 5}, model.getIntArray());
        }, s -> s.enqueue(new MockResponse().setBody("data: " + jsonObject).setHeader(HttpHeaders.CONTENT_TYPE, ContentType.TEXT_EVENT_STREAM)));
    }

    private static class MyApiClient extends JsonApiAsyncClient {
        public MyApiClient(final HttpAsyncClient httpClient, final String uriBase, final ObjectMapper mapper) {
            super(httpClient, true, mapper);
            setUriResolver(new RelativeUriResolver(uriBase));
        }

        public Future<ObjectNode> object(final FutureCallback<ObjectNode> callback) throws ApiException, IOException {
            return execute(get("/ping.html").build(), ObjectNode.class, callback);
        }

        public Future<Model> model(final FutureCallback<Model> callback) throws ApiException, IOException {
            return execute(get("/ping.html").build(), Model.class, callback);
        }

        public Future<Model> model(final ModelParams params, final FutureCallback<Model> callback) throws ApiException, IOException {
            return execute(post("/ping.html").setEntity(jsonEntity(params)).build(), Model.class, callback);
        }

        public Future<ArrayNode> array(final FutureCallback<ArrayNode> callback) throws ApiException, IOException {
            return execute(get("/ping.html").build(), ArrayNode.class, callback);
        }

        public Future<List<Model>> models(final FutureCallback<List<Model>> callback) throws ApiException, IOException {
            return executeArray(get("/ping.html").build(), Model.class, callback);
        }

        public Future<Void> streamModel(final StreamFutureCallback<Model> callback) throws ApiException, IOException {
            return execute(get("/ping.html").build(), Model.class, callback);
        }

        public Future<Void> stream(final StreamFutureCallback<JsonNode> callback) throws ApiException, IOException {
            return execute(get("/ping.html").build(), JsonNode.class, callback);
        }

        public Future<Void> event(final EventStreamFutureCallback<JsonNode> callback) throws ApiException, IOException {
            return execute(get("/ping.html").build(), JsonNode.class, callback);
        }

        public Future<Void> eventModel(final EventStreamFutureCallback<Model> callback) throws ApiException, IOException {
            return execute(get("/ping.html").build(), Model.class, callback);
        }
    }

    private static class MyFutureCallback<T> implements FutureCallback<T> {
        private final Object lock = new Object();

        private volatile boolean called;

        private volatile T result;
        private volatile Exception ex;
        private volatile boolean cancelled;

        @Override
        public void completed(T result) {
            this.result = result;
            synchronized (lock) {
                this.called = true;
                lock.notify();
            }
        }

        @Override
        public void failed(Exception ex) {
            this.ex = ex;
            synchronized (lock) {
                this.called = true;
                lock.notify();
            }
        }

        @Override
        public void cancelled() {
            this.cancelled = true;
            synchronized (lock) {
                this.called = true;
                lock.notify();
            }
        }

        public void assertResult(T result) {
            waitCall();
            assertEquals(result, this.result);
        }

        public void assertException(Throwable ex) {
            waitCall();
            assertSame(ex, this.ex);
        }

        private void waitCall() {
            while (!called) {
                synchronized (lock) {
                    try {
                        lock.wait(100);
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
            }
        }
    }

    private static class MyStreamFutureCallback<T> extends MyFutureCallback<Void>
            implements StreamFutureCallback<T> {
        protected final List<T> results = new ArrayList<>();

        @Override
        public void stream(HttpResponse response, ContentType contentType, boolean endOfStream, T result) {
            results.add(result);
        }
    }

    private static class MyIgnoreEventStreamFutureCallback<T> extends MyStreamFutureCallback<T>
            implements EventStreamFutureCallback<T> {
        @Override
        public boolean ignore(EventStream eventStream) {
            return "ignore".equals(eventStream.getData());
        }
    }

    private static class MyEventStreamFutureCallback<T> extends MyStreamFutureCallback<T>
            implements EventStreamFutureCallback<T> {
    }
}