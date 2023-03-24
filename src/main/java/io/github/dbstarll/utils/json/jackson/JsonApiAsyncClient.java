package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.utils.net.api.ApiAsyncClient;
import org.apache.hc.client5.http.async.HttpAsyncClient;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

public abstract class JsonApiAsyncClient extends ApiAsyncClient {
    protected final ObjectMapper mapper;

    protected JsonApiAsyncClient(final HttpAsyncClient httpClient, final boolean alwaysProcessEntity,
                                 final ObjectMapper mapper) {
        super(httpClient);
        this.mapper = mapper;
        setResponseHandlerFactory(new JsonResponseHandlerFactory(mapper, alwaysProcessEntity));
    }

    protected final <T> Future<T> execute(final ClassicHttpRequest request, final JavaType javaType,
                                          final FutureCallback<T> callback) throws IOException {
        final HttpClientResponseHandler<T> responseHandler = new JavaTypeResponseHandler<>(mapper,
                getResponseHandler(JsonNode.class), javaType, (handlerResult, convertResult) -> {
            logger.trace("handler: [{}]@{} with {}:[{}]", request, request.hashCode(),
                    handlerResult.getClass().getName(), handlerResult);
            logger.trace("convert: [{}]@{} with {}:{}", request, request.hashCode(), javaType, convertResult);
        });
        return execute(request, responseHandler, callback);
    }

    protected final <T> Future<T> executeObject(final ClassicHttpRequest request, final Class<T> responseClass,
                                                final FutureCallback<T> callback)
            throws IOException {
        return execute(request, mapper.getTypeFactory().constructType(responseClass), callback);
    }

    protected final <T> Future<List<T>> executeArray(final ClassicHttpRequest request, final Class<T> responseClass,
                                                     final FutureCallback<List<T>> callback)
            throws IOException {
        return execute(request, mapper.getTypeFactory().constructCollectionType(List.class, responseClass), callback);
    }
}
