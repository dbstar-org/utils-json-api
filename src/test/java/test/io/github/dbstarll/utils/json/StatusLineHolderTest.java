package test.io.github.dbstarll.utils.json;

import io.github.dbstarll.utils.json.StatusLineHolder;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicStatusLine;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StatusLineHolderTest {
    @Test
    public void statusLine() {
        assertNull(StatusLineHolder.getStatusLine());

        final StatusLine statusLine = new BasicStatusLine(HttpVersion.HTTP_1_1, 200, "ok");
        StatusLineHolder.setStatusLine(statusLine);
        final StatusLine statusLine2 = StatusLineHolder.getStatusLine();
        assertEquals(statusLine, statusLine2);

        StatusLineHolder.clearStatusLine();
        assertNull(StatusLineHolder.getStatusLine());
    }
}