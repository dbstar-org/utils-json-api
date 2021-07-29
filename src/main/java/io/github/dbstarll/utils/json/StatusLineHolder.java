package io.github.dbstarll.utils.json;

import org.apache.http.StatusLine;

public final class StatusLineHolder {
    private static final ThreadLocal<StatusLine> holder = new ThreadLocal<StatusLine>();

    private StatusLineHolder() {
        // 禁止实例化
    }

    public static StatusLine getStatusLine() {
        return holder.get();
    }

    public static void setStatusLine(StatusLine statusLine) {
        holder.set(statusLine);
    }
}
