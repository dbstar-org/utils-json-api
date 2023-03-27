package io.github.dbstarll.utils.json;

import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.impl.classic.AbstractHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.StatusLine;

import java.io.IOException;

public final class JsonResponseHandler<T> extends AbstractHttpClientResponseHandler<T> {
    private final JsonParser<T> jsonParser;
    private final boolean alwaysProcessEntity;

    private JsonResponseHandler(final JsonParser<T> jsonParser, final boolean alwaysProcessEntity) {
        this.jsonParser = jsonParser;
        this.alwaysProcessEntity = alwaysProcessEntity;
    }

    @Override
    public T handleResponse(final ClassicHttpResponse response) throws IOException {
        final HttpEntity entity = response.getEntity();
        if (response.getCode() >= HttpStatus.SC_REDIRECTION && (entity == null || !alwaysProcessEntity)) {
            EntityUtils.consume(entity);
            throw new HttpResponseException(response.getCode(), response.getReasonPhrase());
        }
        StatusLineHolder.setStatusLine(new StatusLine(response));
        return entity == null ? null : handleEntity(entity);
    }

    @Override
    public T handleEntity(final HttpEntity entity) throws IOException {
        return jsonParser.parse(new BasicHttpClientResponseHandler().handleEntity(entity));
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
        return new JsonResponseHandler<>(jsonParser, alwaysProcessEntity);
    }
}
