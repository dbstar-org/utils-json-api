package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.utils.net.api.ApiClient;
import org.apache.http.client.HttpClient;

public abstract class JsonApiClient extends ApiClient {
  public JsonApiClient(HttpClient httpClient, ObjectMapper mapper) {
    super(httpClient);
    setResponseHandlerFactory(new JsonResponseHandlerFactory(mapper));
  }

  public JsonApiClient(HttpClient httpClient, ObjectMapper mapper, boolean alwaysProcessEntity) {
    super(httpClient);
    setResponseHandlerFactory(new JsonResponseHandlerFactory(mapper, alwaysProcessEntity));
  }
}
