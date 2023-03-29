package io.github.dbstarll.utils.json.jackson;

import io.github.dbstarll.utils.net.api.index.AbstractIndex;
import io.github.dbstarll.utils.net.api.index.EventStream;
import io.github.dbstarll.utils.net.api.index.EventStreamIndex;
import io.github.dbstarll.utils.net.api.index.Index;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.support.ClassicResponseBuilder;

import java.io.IOException;

final class EventStreamResponseHandler<T> implements HttpClientResponseHandler<Index<T>> {
    private final HttpClientResponseHandler<EventStreamIndex> from;
    private final HttpClientResponseHandler<T> target;

    EventStreamResponseHandler(final HttpClientResponseHandler<EventStreamIndex> from,
                               final HttpClientResponseHandler<T> target) {
        this.from = from;
        this.target = target;
    }

    @Override
    public Index<T> handleResponse(final ClassicHttpResponse response) throws HttpException, IOException {
        final EventStreamIndex eventStreamIndex = from.handleResponse(response);
        if (eventStreamIndex == null) {
            return null;
        }

        final EventStream eventStream = eventStreamIndex.getData();
        final T data = eventStream == null || StringUtils.isBlank(eventStream.getData()) ? null
                : handleResponse(response, eventStream.getData());
        return new AbstractIndex<T>(data, eventStreamIndex.getIndex()) {
        };
    }

    private T handleResponse(final HttpResponse response, final String content) throws IOException, HttpException {
        final ClassicHttpResponse classicHttpResponse = ClassicResponseBuilder.create(response.getCode())
                .setVersion(response.getVersion())
                .setHeaders(response.getHeaders())
                .setEntity(content)
                .build();
        return target.handleResponse(classicHttpResponse);
    }
}
