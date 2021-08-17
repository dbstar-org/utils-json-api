package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.utils.net.api.ApiClient;
import org.apache.http.client.HttpClient;

public abstract class JsonApiClient extends ApiClient {
    protected JsonApiClient(final HttpClient httpClient, final ObjectMapper mapper) {
        super(httpClient);
        setResponseHandlerFactory(new JsonResponseHandlerFactory(mapper));
    }

    protected JsonApiClient(final HttpClient httpClient, final ObjectMapper mapper, final boolean alwaysProcessEntity) {
        super(httpClient);
        setResponseHandlerFactory(new JsonResponseHandlerFactory(mapper, alwaysProcessEntity));
    }
}
