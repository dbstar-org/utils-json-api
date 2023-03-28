package io.github.dbstarll.utils.json.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.utils.json.JsonParseException;
import io.github.dbstarll.utils.net.api.index.Index;
import io.github.dbstarll.utils.net.api.index.IndexBaseHttpClientResponseHandler;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.function.BiConsumer;

final class StreamJavaTypeResponseHandler<T> extends IndexBaseHttpClientResponseHandler<Index<T>> {
    private final ObjectMapper mapper;
    private final JavaType javaType;
    private final BiConsumer<String, T> consumer;

    StreamJavaTypeResponseHandler(final ObjectMapper mapper, final JavaType javaType,
                                  final BiConsumer<String, T> consumer) {
        this.mapper = mapper;
        this.javaType = javaType;
        this.consumer = consumer;
    }

    @Override
    protected Index<T> handleContent(final String content, final boolean endOfStream) throws IOException {
        if (StringUtils.isBlank(content)) {
            return new JavaTypeIndex<>(null, -1);
        }
        try (JsonParser parser = mapper.createParser(content)) {
            final T data = mapper.readValue(parser, javaType);
            final int index = (int) parser.currentLocation().getCharOffset();
            consumer.accept(content.substring(0, index), data);
            return new JavaTypeIndex<>(data, index);
        } catch (Exception e) {
            if (endOfStream) {
                throw new JsonParseException(e);
            } else {
                return null;
            }
        }
    }
}
