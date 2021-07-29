package io.github.dbstarll.utils.json;

public interface JsonParser<T> {
    T parse(String str) throws JsonParseException;
}
