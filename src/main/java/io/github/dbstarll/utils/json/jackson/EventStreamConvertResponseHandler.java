package io.github.dbstarll.utils.json.jackson;

import io.github.dbstarll.utils.net.api.index.AbstractIndex;
import io.github.dbstarll.utils.net.api.index.EventStream;
import io.github.dbstarll.utils.net.api.index.Index;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.support.ClassicResponseBuilder;

import java.io.IOException;
import java.util.function.Predicate;

final class EventStreamConvertResponseHandler<T> implements HttpClientResponseHandler<Index<T>> {
    private final HttpClientResponseHandler<? extends Index<EventStream>> original;
    private final HttpClientResponseHandler<T> target;
    private final Predicate<EventStream> ignore;

    EventStreamConvertResponseHandler(final HttpClientResponseHandler<? extends Index<EventStream>> original,
                                      final HttpClientResponseHandler<T> target, final Predicate<EventStream> ignore) {
        this.original = original;
        this.target = target;
        this.ignore = ignore;
    }

    @Override
    public Index<T> handleResponse(final ClassicHttpResponse response) throws HttpException, IOException {
        final Index<EventStream> eventStreamIndex = original.handleResponse(response);
        if (eventStreamIndex == null) {
            return null;
        }

        final EventStream eventStream = eventStreamIndex.getData();
        final T data = eventStream == null || StringUtils.isBlank(eventStream.getData()) || ignore.test(eventStream)
                ? null : handleResponse(response, eventStream.getData());
        return new AbstractIndex<T>(data, eventStreamIndex.getIndex()) {
        };
    }

    private T handleResponse(final ClassicHttpResponse response, final String content)
            throws IOException, HttpException {
        final ClassicHttpResponse classicHttpResponse = ClassicResponseBuilder.create(response.getCode())
                .setVersion(response.getVersion())
                .setHeaders(response.getHeaders())
                .setEntity(buildEntity(response, content))
                .build();
        return target.handleResponse(classicHttpResponse);
    }

    private HttpEntity buildEntity(final ClassicHttpResponse response, final String content) {
        final ContentType contentType = ContentType.parse(response.getEntity().getContentType());
        if (contentType != null) {
            return new StringEntity(content, contentType);
        }
        return new StringEntity(content);
    }
}
