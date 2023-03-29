package io.github.dbstarll.utils.json.jackson;

import io.github.dbstarll.utils.net.api.StreamFutureCallback;
import io.github.dbstarll.utils.net.api.index.EventStream;

public interface EventStreamFutureCallback<T> extends StreamFutureCallback<T> {
    /**
     * 测试是否忽略指定的eventStream.
     *
     * @param eventStream 被测试的EventStream
     * @return 是否忽略
     */
    default boolean ignore(final EventStream eventStream) {
        return false;
    }
}
