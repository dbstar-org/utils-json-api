package io.github.dbstarll.utils.json.json;

import io.github.dbstarll.utils.net.api.ApiClient;
import org.apache.http.client.HttpClient;

public abstract class JsonApiClient extends ApiClient {
    protected JsonApiClient(final HttpClient httpClient) {
        super(httpClient);
        setResponseHandlerFactory(new JsonResponseHandlerFactory());
    }
}
