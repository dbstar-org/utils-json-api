package io.github.dbstarll.utils.json;

import io.github.dbstarll.utils.http.client.HttpClientFactory;
import io.github.dbstarll.utils.json.test.Model;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.ThrowingConsumer;

public abstract class JsonApiClientTestCase {
    protected Model model1;
    protected Model model2;

    @BeforeEach
    protected void setUp() throws Exception {
        this.model1 = new Model(100, "stringValue1", true, 3.14f, new int[]{1, 2, 3, 4, 5});
        this.model2 = new Model(101, "stringValue2", false, 1.41f, new int[]{5, 4, 3, 2, 1});
    }

    @AfterEach
    protected void tearDown() {
        this.model1 = null;
        this.model2 = null;
    }

    protected final void useServer(final ThrowingConsumer<MockWebServer> consumer,
                                   final ThrowingConsumer<MockWebServer> customizer) throws Throwable {
        try (final MockWebServer server = new MockWebServer()) {
            customizer.accept(server);
            server.start();
            consumer.accept(server);
        }
    }


    protected final void useClient(final ThrowingBiConsumer<MockWebServer, HttpClient> consumer,
                                   final ThrowingConsumer<MockWebServer> customizer) throws Throwable {
        useServer(server -> {
            try (CloseableHttpClient client = new HttpClientFactory().build()) {
                consumer.accept(server, client);
            }
        }, customizer);
    }
}
