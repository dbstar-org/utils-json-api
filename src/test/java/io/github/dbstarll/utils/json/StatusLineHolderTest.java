package io.github.dbstarll.utils.json;

import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicStatusLine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StatusLineHolderTest {
    @Test
    void statusLine() {
        assertNull(StatusLineHolder.getStatusLine());

        final StatusLine statusLine = new BasicStatusLine(HttpVersion.HTTP_1_1, 200, "ok");
        StatusLineHolder.setStatusLine(statusLine);
        final StatusLine statusLine2 = StatusLineHolder.getStatusLine();
        assertEquals(statusLine, statusLine2);

        StatusLineHolder.clearStatusLine();
        assertNull(StatusLineHolder.getStatusLine());
    }
}