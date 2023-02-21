package io.github.dbstarll.utils.json;

import io.github.dbstarll.utils.http.client.response.AbstractResponseHandlerFactory;

public abstract class JsonParserResponseHandlerFactory extends AbstractResponseHandlerFactory {
    protected final <T> void addResponseHandler(final Class<T> responseClass, final JsonParser<T> jsonParser) {
        addResponseHandler(responseClass, JsonResponseHandler.create(jsonParser));
    }

    protected final <T> void addResponseHandler(final Class<T> responseClass, final JsonParser<T> jsonParser,
                                                final boolean alwaysProcessEntity) {
        addResponseHandler(responseClass, JsonResponseHandler.create(jsonParser, alwaysProcessEntity));
    }
}
