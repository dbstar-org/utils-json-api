package io.github.dbstarll.utils.json.jackson;

import io.github.dbstarll.utils.net.api.index.AbstractIndex;

class JavaTypeIndex<T> extends AbstractIndex<T> {
    JavaTypeIndex(final T data, final int index) {
        super(data, index);
    }
}
