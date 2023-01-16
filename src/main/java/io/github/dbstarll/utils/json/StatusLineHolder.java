package io.github.dbstarll.utils.json;

import org.apache.http.StatusLine;

public final class StatusLineHolder {
    private static final ThreadLocal<StatusLine> HOLDER = new ThreadLocal<>();

    private StatusLineHolder() {
        // 禁止实例化
    }

    /**
     * 获取StatusLine.
     *
     * @return StatusLine
     */
    public static StatusLine getStatusLine() {
        return HOLDER.get();
    }

    /**
     * 设置StatusLine.
     *
     * @param statusLine StatusLine
     */
    public static void setStatusLine(final StatusLine statusLine) {
        HOLDER.set(statusLine);
    }

    /**
     * 清除之前设置的StatusLine.
     */
    public static void clearStatusLine() {
        HOLDER.remove();
    }
}
