package io.github.dbstarll.utils.json;

import okhttp3.mockwebserver.MockResponse;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.io.support.ClassicResponseBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

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
            final ClassicHttpRequest request = ClassicRequestBuilder.get(s.url("/ping.html").uri()).build();
            final HttpClientResponseHandler<String> responseHandler = JsonResponseHandler.create(stringParser, false);
            assertEquals("ok", c.execute(request, responseHandler));
        }, s -> s.enqueue(new MockResponse().setBody("ok")));
    }

    @Test
    void handleNoEntity() throws IOException, HttpException {
        final HttpClientResponseHandler<String> responseHandler = JsonResponseHandler.create(stringParser, false);
        final ClassicHttpResponse response = ClassicResponseBuilder.create(200).build();
        assertNull(responseHandler.handleResponse(response));
    }

    @Test
    void errorNoEntity() throws IOException {
        final HttpClientResponseHandler<String> responseHandler = JsonResponseHandler.create(stringParser, false);
        try (final ClassicHttpResponse response = ClassicResponseBuilder.create(404).build()) {
            final Exception e = assertThrowsExactly(HttpResponseException.class, () -> responseHandler.handleResponse(response));
            assertEquals("status code: 404, reason phrase: Not Found", e.getMessage());
        }
    }

    @Test
    void errorWithEntity() throws Throwable {
        useClient((s, c) -> {
            final ClassicHttpRequest request = ClassicRequestBuilder.get(s.url("/ping.html").uri()).build();
            final HttpClientResponseHandler<String> responseHandler = JsonResponseHandler.create(stringParser, false);
            final Exception e = assertThrowsExactly(HttpResponseException.class, () -> c.execute(request, responseHandler));
            assertEquals("status code: 404, reason phrase: Client Error", e.getMessage());
        }, s -> s.enqueue(new MockResponse().setResponseCode(404).setBody("ok")));
    }

    @Test
    void errorWithProcessEntity() throws Throwable {
        useClient((s, c) -> {
            final ClassicHttpRequest request = ClassicRequestBuilder.get(s.url("/ping.html").uri()).build();
            final HttpClientResponseHandler<String> responseHandler = JsonResponseHandler.create(stringParser, true);
            assertEquals("ok", c.execute(request, responseHandler));
        }, s -> s.enqueue(new MockResponse().setResponseCode(404).setBody("ok")));
    }
}