package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.utils.net.api.index.Index;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.io.IOException;
import java.util.function.BiConsumer;

final class StreamJavaTypeResponseHandler<H extends Index<HD>, HD, TD>
        implements HttpClientResponseHandler<JavaTypeIndex<TD>> {
    private final ObjectMapper mapper;
    private final HttpClientResponseHandler<H> responseHandler;
    private final JavaType javaType;
    private final BiConsumer<HD, TD> consumer;

    StreamJavaTypeResponseHandler(final ObjectMapper mapper, final HttpClientResponseHandler<H> responseHandler,
                                  final JavaType javaType, final BiConsumer<HD, TD> consumer) {
        this.mapper = mapper;
        this.responseHandler = responseHandler;
        this.javaType = javaType;
        this.consumer = consumer;
    }

    @Override
    public JavaTypeIndex<TD> handleResponse(final ClassicHttpResponse response) throws HttpException, IOException {
        final H handlerResult = responseHandler.handleResponse(response);
        if (handlerResult.getData() == null) {
            return new JavaTypeIndex<>(null, handlerResult.getIndex());
        } else {
            final TD convertResult = mapper.convertValue(handlerResult.getData(), javaType);
            consumer.accept(handlerResult.getData(), convertResult);
            return new JavaTypeIndex<>(convertResult, handlerResult.getIndex());
        }
    }
}
