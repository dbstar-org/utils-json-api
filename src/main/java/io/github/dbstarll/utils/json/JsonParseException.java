package io.github.dbstarll.utils.json;

import java.io.IOException;

public class JsonParseException extends IOException {
    private static final long serialVersionUID = -7400009992178945785L;

    /**
     * 构建JsonParseException.
     *
     * @param cause The cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A null value is permitted,
     *              and indicates that the cause is nonexistent or unknown.)
     */
    public JsonParseException(final Throwable cause) {
        super(cause);
    }
}
