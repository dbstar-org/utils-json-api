package io.github.dbstarll.utils.json;

@FunctionalInterface
public interface ThrowingBiConsumer<T, U> {
    /**
     * Consume the supplied argument, potentially throwing an exception.
     *
     * @param t the first input argument
     * @param u the second input argument
     */
    void accept(T t, U u) throws Throwable;
}
