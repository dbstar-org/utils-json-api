package io.github.dbstarll.utils.json;

import org.apache.hc.core5.http.HttpVersion;
import org.apache.hc.core5.http.message.StatusLine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StatusLineHolderTest {
    @Test
    void statusLine() throws InterruptedException {
        final Thread thread = new Thread(() -> assertNull(StatusLineHolder.getStatusLine()));
        thread.start();
        thread.join();

        final StatusLine statusLine = new StatusLine(HttpVersion.HTTP_1_1, 200, "ok");
        StatusLineHolder.setStatusLine(statusLine);
        final StatusLine statusLine2 = StatusLineHolder.getStatusLine();
        assertEquals(statusLine, statusLine2);

        StatusLineHolder.clearStatusLine();
        assertNull(StatusLineHolder.getStatusLine());
    }
}