package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.utils.net.api.ApiClient;
import io.github.dbstarll.utils.net.api.ApiException;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.entity.EntityBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.io.IOException;
import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

public abstract class JsonApiClient extends ApiClient {
    protected final ObjectMapper mapper;

    protected JsonApiClient(final HttpClient httpClient, final boolean alwaysProcessEntity, final ObjectMapper mapper) {
        super(httpClient, alwaysProcessEntity);
        this.mapper = mapper;
        setResponseHandlerFactory(new JsonResponseHandlerFactory(mapper, alwaysProcessEntity));
    }

    protected final <T> HttpEntity jsonEntity(final T request) throws JsonProcessingException {
        return EntityBuilder.create().setText(mapper.writeValueAsString(request))
                .setContentType(ContentType.APPLICATION_JSON).setContentEncoding("UTF-8").build();
    }

    protected final <T> T execute(final ClassicHttpRequest request, final JavaType javaType)
            throws IOException, ApiException {
        final JsonNode executeResult = super.execute(request, JsonNode.class);
        final T convertResult = mapper.convertValue(executeResult, javaType);
        logger.trace("convert: [{}]@{} with {}:{}", request, request.hashCode(), javaType, convertResult);
        return convertResult;
    }

    protected final <T> T execute(final ClassicHttpRequest request, final TypeReference<T> typeReference)
            throws IOException, ApiException {
        return execute(request, mapper.constructType(typeReference));
    }

    @Override
    protected final <T> T execute(final ClassicHttpRequest request, final Class<T> responseClass)
            throws IOException, ApiException {
        notNull(responseClass, "responseClass is null");
        final HttpClientResponseHandler<T> responseHandler = getResponseHandler(responseClass);
        if (responseHandler != null) {
            return execute(request, responseHandler);
        } else {
            return execute(request, mapper.constructType(responseClass));
        }
    }

    protected final <T> List<T> executeArray(final ClassicHttpRequest request, final Class<T> responseClass)
            throws IOException, ApiException {
        return execute(request, mapper.getTypeFactory().constructCollectionType(List.class, responseClass));
    }
}
