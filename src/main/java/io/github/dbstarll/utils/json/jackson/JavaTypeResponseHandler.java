package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.io.IOException;
import java.util.function.BiConsumer;

final class JavaTypeResponseHandler<H, T> implements HttpClientResponseHandler<T> {
    private final ObjectMapper mapper;
    private final HttpClientResponseHandler<H> responseHandler;
    private final JavaType javaType;
    private final BiConsumer<H, T> consumer;

    JavaTypeResponseHandler(final ObjectMapper mapper, final HttpClientResponseHandler<H> responseHandler,
                            final JavaType javaType, final BiConsumer<H, T> consumer) {
        this.mapper = mapper;
        this.responseHandler = responseHandler;
        this.javaType = javaType;
        this.consumer = consumer;
    }

    @Override
    public T handleResponse(final ClassicHttpResponse response) throws HttpException, IOException {
        final H handlerResult = responseHandler.handleResponse(response);
        final T convertResult = mapper.convertValue(handlerResult, javaType);
        consumer.accept(handlerResult, convertResult);
        return convertResult;
    }
}
