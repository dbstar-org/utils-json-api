package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.utils.net.api.ApiClient;
import io.github.dbstarll.utils.net.api.ApiException;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.core5.http.ClassicHttpRequest;

import java.io.IOException;
import java.util.List;

public abstract class JsonApiClient extends ApiClient {
    protected final ObjectMapper mapper;

    protected JsonApiClient(final HttpClient httpClient, final boolean alwaysProcessEntity, final ObjectMapper mapper) {
        super(httpClient);
        this.mapper = mapper;
        setResponseHandlerFactory(new JsonResponseHandlerFactory(mapper, alwaysProcessEntity));
    }

    protected final <T> T execute(final ClassicHttpRequest request, final JavaType javaType)
            throws IOException, ApiException {
        final JsonNode executeResult = super.execute(request, JsonNode.class);
        final T convertResult = mapper.convertValue(executeResult, javaType);
        logger.trace("convert: [{}]@{} with {}:{}", request, request.hashCode(), javaType, convertResult);
        return convertResult;
    }

    protected final <T> T executeObject(final ClassicHttpRequest request, final Class<T> responseClass)
            throws IOException, ApiException {
        return execute(request, mapper.getTypeFactory().constructType(responseClass));
    }

    protected final <T> List<T> executeArray(final ClassicHttpRequest request, final Class<T> responseClass)
            throws IOException, ApiException {
        return execute(request, mapper.getTypeFactory().constructCollectionType(List.class, responseClass));
    }
}
