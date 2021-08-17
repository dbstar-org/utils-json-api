package io.github.dbstarll.utils.json;

public interface JsonParser<T> {
    /**
     * 从字符串解析结果.
     *
     * @param str 字符串
     * @return 解析后的结果
     * @throws JsonParseException json解析异常
     */
    T parse(String str) throws JsonParseException;
}
