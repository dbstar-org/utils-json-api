package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.utils.net.api.ApiAsyncClient;
import io.github.dbstarll.utils.net.api.StreamFutureCallback;
import io.github.dbstarll.utils.net.api.index.EventStreamIndex;
import io.github.dbstarll.utils.net.api.index.Index;
import org.apache.hc.client5.http.async.HttpAsyncClient;
import org.apache.hc.client5.http.entity.EntityBuilder;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;

import static org.apache.commons.lang3.Validate.notNull;

public abstract class JsonApiAsyncClient extends ApiAsyncClient {
    private static final String RESPONSE_CLASS_IS_NULL_EX_MESSAGE = "responseClass is null";
    protected final ObjectMapper mapper;

    protected JsonApiAsyncClient(final HttpAsyncClient httpClient, final boolean alwaysProcessEntity,
                                 final ObjectMapper mapper) {
        super(httpClient, alwaysProcessEntity);
        this.mapper = mapper;
        setResponseHandlerFactory(new JsonResponseHandlerFactory(mapper, alwaysProcessEntity));
        setResponseHandlerFactory(new JsonIndexResponseHandlerFactory(getResponseHandler(String.class), mapper));
    }

    protected final <T> HttpEntity jsonEntity(final T request) throws JsonProcessingException {
        return EntityBuilder.create().setText(mapper.writeValueAsString(request))
                .setContentType(ContentType.APPLICATION_JSON).setContentEncoding("UTF-8").build();
    }

    private <H, C> BiConsumer<H, C> convert(final ClassicHttpRequest request, final JavaType javaType) {
        return (handlerResult, convertResult) -> {
            logger.trace("handler: [{}]@{} with {}:[{}]", request, request.hashCode(),
                    handlerResult.getClass().getName(), handlerResult);
            logger.trace("convert: [{}]@{} with {}:{}", request, request.hashCode(), javaType, convertResult);
        };
    }

    protected final <T> Future<T> execute(final ClassicHttpRequest request, final JavaType javaType,
                                          final FutureCallback<T> callback) throws IOException {
        return execute(request, new JavaTypeResponseHandler<>(getResponseHandler(String.class), mapper, javaType,
                convert(request, javaType)), callback);
    }

    @Override
    protected final <T> Future<T> execute(final ClassicHttpRequest request, final Class<T> responseClass,
                                          final FutureCallback<T> callback) throws IOException {
        notNull(responseClass, RESPONSE_CLASS_IS_NULL_EX_MESSAGE);
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
        return execute(request, new StreamJavaTypeResponseHandler<>(getResponseHandler(String.class), mapper, javaType,
                convert(request, javaType)), callback);
    }

    @Override
    protected final <T> Future<Void> execute(final ClassicHttpRequest request, final Class<T> responseClass,
                                             final StreamFutureCallback<T> callback) throws IOException {
        notNull(responseClass, RESPONSE_CLASS_IS_NULL_EX_MESSAGE);
        final Class<? extends Index<T>> streamResponseClass = getStreamResponseClass(responseClass);
        if (streamResponseClass != null) {
            return execute(request, getResponseHandler(streamResponseClass), callback);
        } else {
            return execute(request, mapper.getTypeFactory().constructType(responseClass), callback);
        }
    }

    protected final <T> Future<Void> execute(final ClassicHttpRequest request,
                                             final HttpClientResponseHandler<T> responseHandler,
                                             final EventStreamFutureCallback<T> callback) throws IOException {
        notNull(responseHandler, "responseHandler is null");
        notNull(callback, "callback is null");
        return execute(request, new EventStreamConvertResponseHandler<>(getResponseHandler(EventStreamIndex.class),
                responseHandler, callback::ignore), callback);
    }

    protected final <T> Future<Void> execute(final ClassicHttpRequest request, final JavaType javaType,
                                             final EventStreamFutureCallback<T> callback) throws IOException {
        return execute(request, new JavaTypeResponseHandler<T>(getResponseHandler(String.class), mapper, javaType,
                convert(request, javaType)), callback);
    }

    protected final <T> Future<Void> execute(final ClassicHttpRequest request, final Class<T> responseClass,
                                             final EventStreamFutureCallback<T> callback) throws IOException {
        notNull(responseClass, RESPONSE_CLASS_IS_NULL_EX_MESSAGE);
        final HttpClientResponseHandler<T> responseHandler = getResponseHandler(responseClass);
        if (responseHandler != null) {
            return execute(request, getResponseHandler(responseClass), callback);
        } else {
            return execute(request, mapper.getTypeFactory().constructType(responseClass), callback);
        }
    }
}
