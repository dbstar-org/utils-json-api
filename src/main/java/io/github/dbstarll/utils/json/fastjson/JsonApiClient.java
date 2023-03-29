package io.github.dbstarll.utils.json.fastjson;

import io.github.dbstarll.utils.net.api.ApiClient;
import org.apache.hc.client5.http.classic.HttpClient;

public abstract class JsonApiClient extends ApiClient {
    protected JsonApiClient(final HttpClient httpClient, final boolean alwaysProcessEntity) {
        super(httpClient, alwaysProcessEntity);
        setResponseHandlerFactory(new JsonResponseHandlerFactory(alwaysProcessEntity));
    }
}
