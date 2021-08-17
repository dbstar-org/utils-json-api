package io.github.dbstarll.utils.json;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public final class JsonResponseHandler<T> extends AbstractResponseHandler<T> {
    private static final int ERROR_STATUS_CODE = 300;
    private final JsonParser<T> jsonParser;
    private final boolean alwaysProcessEntity;

    private JsonResponseHandler(final JsonParser<T> jsonParser, final boolean alwaysProcessEntity) {
        this.jsonParser = jsonParser;
        this.alwaysProcessEntity = alwaysProcessEntity;
    }

    @Override
    public T handleResponse(final HttpResponse response) throws HttpResponseException, IOException {
        final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= ERROR_STATUS_CODE && (entity == null || !alwaysProcessEntity)) {
            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        StatusLineHolder.setStatusLine(statusLine);
        return entity == null ? null : handleEntity(entity);
    }

    @Override
    public T handleEntity(final HttpEntity entity) throws IOException {
        return jsonParser.parse(EntityUtils.toString(entity));
    }

    /**
     * 构建JsonResponseHandler.
     *
     * @param jsonParser json解析器
     * @param <T>        解析结果类型
     * @return JsonResponseHandler
     */
    public static <T> JsonResponseHandler<T> create(final JsonParser<T> jsonParser) {
        return create(jsonParser, false);
    }

    /**
     * 构建JsonResponseHandler.
     *
     * @param jsonParser          json解析器
     * @param alwaysProcessEntity 在返回错误的状态码时，是否还要继续解析entity
     * @param <T>                 解析结果类型
     * @return JsonResponseHandler
     */
    public static <T> JsonResponseHandler<T> create(final JsonParser<T> jsonParser, final boolean alwaysProcessEntity) {
        return new JsonResponseHandler<T>(jsonParser, alwaysProcessEntity);
    }
}
