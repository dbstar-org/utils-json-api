package io.github.dbstarll.utils.json;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public final class JsonResponseHandler<T> extends AbstractResponseHandler<T> {
    private final JsonParser<T> jsonParser;
    private final boolean alwaysProcessEntity;

    private JsonResponseHandler(JsonParser<T> jsonParser, boolean alwaysProcessEntity) {
        this.jsonParser = jsonParser;
        this.alwaysProcessEntity = alwaysProcessEntity;
    }

    @Override
    public T handleResponse(HttpResponse response) throws HttpResponseException, IOException {
        final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= 300 && (entity == null || !alwaysProcessEntity)) {
            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        StatusLineHolder.setStatusLine(statusLine);
        return entity == null ? null : handleEntity(entity);
    }

    @Override
    public T handleEntity(HttpEntity entity) throws IOException {
        return jsonParser.parse(EntityUtils.toString(entity));
    }

    public static <T> JsonResponseHandler<T> create(JsonParser<T> jsonParser) {
        return create(jsonParser, false);
    }

    public static <T> JsonResponseHandler<T> create(JsonParser<T> jsonParser, boolean alwaysProcessEntity) {
        return new JsonResponseHandler<T>(jsonParser, alwaysProcessEntity);
    }
}
