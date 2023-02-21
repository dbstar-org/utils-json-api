package io.github.dbstarll.utils.json;

import okhttp3.mockwebserver.MockResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicHttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class JsonResponseHandlerTest extends JsonApiClientTestCase {
    private JsonParser<String> stringParser;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.stringParser = str -> str;
    }

    @AfterEach
    @Override
    protected void tearDown() {
        this.stringParser = null;
        super.tearDown();
    }

    @Test
    void handleResponse() throws Throwable {
        useClient((s, c) -> {
            final HttpUriRequest request = RequestBuilder.get(s.url("/ping.html").uri()).build();
            final ResponseHandler<String> responseHandler = JsonResponseHandler.create(stringParser, false);
            assertEquals("ok", c.execute(request, responseHandler));
        }, s -> s.enqueue(new MockResponse().setBody("ok")));
    }

    @Test
    void handleNoEntity() throws IOException {
        final ResponseHandler<String> responseHandler = JsonResponseHandler.create(stringParser, false);
        final BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, 200, "ok");
        assertNull(responseHandler.handleResponse(response));
    }

    @Test
    void errorNoEntity() {
        final ResponseHandler<String> responseHandler = JsonResponseHandler.create(stringParser, false);
        final BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, 404, "Client Error");
        final Exception e = assertThrowsExactly(HttpResponseException.class, () -> responseHandler.handleResponse(response));
        assertEquals("status code: 404, reason phrase: Client Error", e.getMessage());
    }

    @Test
    void errorWithEntity() throws Throwable {
        useClient((s, c) -> {
            final HttpUriRequest request = RequestBuilder.get(s.url("/ping.html").uri()).build();
            final ResponseHandler<String> responseHandler = JsonResponseHandler.create(stringParser, false);
            final Exception e = assertThrowsExactly(HttpResponseException.class, () -> c.execute(request, responseHandler));
            assertEquals("status code: 404, reason phrase: Client Error", e.getMessage());
        }, s -> s.enqueue(new MockResponse().setResponseCode(404).setBody("ok")));
    }

    @Test
    void errorWithProcessEntity() throws Throwable {
        useClient((s, c) -> {
            final HttpUriRequest request = RequestBuilder.get(s.url("/ping.html").uri()).build();
            final ResponseHandler<String> responseHandler = JsonResponseHandler.create(stringParser, true);
            assertEquals("ok", c.execute(request, responseHandler));
        }, s -> s.enqueue(new MockResponse().setResponseCode(404).setBody("ok")));
    }
}