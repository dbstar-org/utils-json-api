package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.io.IOException;
import java.util.function.BiConsumer;

final class JavaTypeResponseHandler<T> implements HttpClientResponseHandler<T> {
    private static final BasicHttpClientResponseHandler DEFAULT_HANDLER = new BasicHttpClientResponseHandler();

    private final ObjectMapper mapper;
    private final JavaType javaType;
    private final BiConsumer<String, T> consumer;

    JavaTypeResponseHandler(final ObjectMapper mapper, final JavaType javaType, final BiConsumer<String, T> consumer) {
        this.mapper = mapper;
        this.javaType = javaType;
        this.consumer = consumer;
    }

    @Override
    public T handleResponse(final ClassicHttpResponse response) throws IOException {
        final String handlerResult = DEFAULT_HANDLER.handleResponse(response);
        final T convertResult = mapper.readValue(handlerResult, javaType);
        consumer.accept(handlerResult, convertResult);
        return convertResult;
    }
}
