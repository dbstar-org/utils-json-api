package io.github.dbstarll.utils.json.gson;

import com.google.gson.Gson;
import io.github.dbstarll.utils.net.api.ApiClient;
import org.apache.http.client.HttpClient;

public abstract class JsonApiClient extends ApiClient {
    public JsonApiClient(HttpClient httpClient, Gson gson) {
        super(httpClient);
        setResponseHandlerFactory(new JsonResponseHandlerFactory(gson));
    }
}
