package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.utils.net.api.ApiAsyncClient;
import io.github.dbstarll.utils.net.api.StreamFutureCallback;
import io.github.dbstarll.utils.net.api.index.Index;
import org.apache.hc.client5.http.async.HttpAsyncClient;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import static org.apache.commons.lang3.Validate.notNull;

public abstract class JsonApiAsyncClient extends ApiAsyncClient {
    protected final ObjectMapper mapper;

    protected JsonApiAsyncClient(final HttpAsyncClient httpClient, final boolean alwaysProcessEntity,
                                 final ObjectMapper mapper) {
        super(httpClient);
        this.mapper = mapper;
        setResponseHandlerFactory(new JsonResponseHandlerFactory(mapper, alwaysProcessEntity));
        setResponseHandlerFactory(new JsonIndexResponseHandlerFactory(mapper));
    }

    protected final <T> Future<T> execute(final ClassicHttpRequest request, final JavaType javaType,
                                          final FutureCallback<T> callback) throws IOException {
        return execute(request, new JavaTypeResponseHandler<>(mapper, getResponseHandler(String.class), javaType,
                (handlerResult, convertResult) -> {
                    logger.trace("handler: [{}]@{} with {}:[{}]", request, request.hashCode(),
                            handlerResult.getClass().getName(), handlerResult);
                    logger.trace("convert: [{}]@{} with {}:{}", request, request.hashCode(), javaType, convertResult);
                }), callback);
    }

    @Override
    protected final <T> Future<T> execute(final ClassicHttpRequest request, final Class<T> responseClass,
                                          final FutureCallback<T> callback) throws IOException {
        notNull(responseClass, "responseClass is null");
        final HttpClientResponseHandler<T> responseHandler = getResponseHandler(responseClass);
        if (responseHandler != null) {
            return execute(request, responseHandler, callback);
        } else {
            return execute(request, mapper.getTypeFactory().constructType(responseClass), callback);
        }
    }

    protected final <T> Future<List<T>> executeArray(final ClassicHttpRequest request, final Class<T> responseClass,
                                                     final FutureCallback<List<T>> callback) throws IOException {
        return execute(request, mapper.getTypeFactory().constructCollectionType(List.class, responseClass), callback);
    }

    protected final <T> Future<Void> execute(final ClassicHttpRequest request, final JavaType javaType,
                                             final StreamFutureCallback<T> callback) throws IOException {
        return execute(request, new StreamJavaTypeResponseHandler<>(mapper, javaType,
                (handlerResult, convertResult) -> {
                    logger.trace("handler: [{}]@{} with {}:[{}]", request, request.hashCode(),
                            handlerResult.getClass().getName(), handlerResult);
                    logger.trace("convert: [{}]@{} with {}:{}", request, request.hashCode(), javaType, convertResult);
                }), callback);
    }

    @Override
    protected final <T> Future<Void> execute(final ClassicHttpRequest request, final Class<T> responseClass,
                                             final StreamFutureCallback<T> callback) throws IOException {
        notNull(responseClass, "responseClass is null");
        final Class<? extends Index<T>> streamResponseClass = getStreamResponseClass(responseClass);
        if (streamResponseClass != null) {
            return execute(request, getResponseHandler(streamResponseClass), callback);
        } else {
            return execute(request, mapper.getTypeFactory().constructType(responseClass), callback);
        }
    }
}
