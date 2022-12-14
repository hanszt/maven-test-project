package org.hzt.concurrent;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/**
 * @author Hans Zuidervaart
 * A test to get familiar with awaitility
 * and other concurency principles
 * @see <a href="https://www.baeldung.com/awaitlity-testing">Introduction to Awaitility</a>
 */
class AsyncServiceTest {

    private AsyncService asyncService;

    @BeforeEach
    public void setUp() {
        asyncService = new AsyncService();
    }

    @Test
    void testInitializeWithCustomProperties() {
        Awaitility.setDefaultPollInterval(10, TimeUnit.MILLISECONDS);
        Awaitility.setDefaultPollDelay(Duration.ZERO);
        Awaitility.setDefaultTimeout(Duration.ofSeconds(5));
        asyncService.initialize();
        await().until(asyncService::isInitialized);
    }

    @Test
    void testInitialize() {
        asyncService.initialize();
        await().atLeast(Duration.ofMillis(100))
                .atMost(Duration.ofSeconds(5))
                .with()
                .pollInterval(Duration.ofMillis(100))
                .until(asyncService::isInitialized);
    }

    @Test
    void testInitializeWithArgumentMatchers() {
        asyncService.initialize();
        await().until(asyncService::isInitialized);
        long value = 5;
        asyncService.addValue(value);
        await().until(asyncService::getValue, equalTo(value));
    }

    @Test
    void testIgnoringExceptions() {
        asyncService.initialize();
        given().ignoreException(IllegalStateException.class)
                .await().atMost(Duration.ofSeconds(1))
                .atLeast(Duration.ofMillis(100))
                .until(asyncService::getValue, equalTo(0L));
    }

    @Test
    void whenAssertingTimeout_thenNotExceeded() {
        var service = new AsyncService(true);
        assertTimeout(Duration.ofSeconds(1), () -> addValues(service));
    }

    @Test
    void whenAssertingTimeoutPreemptively_thenNotExceeded() {
        var service = new AsyncService(true);
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> addValues(service));
    }

    private void addValues(AsyncService service) {
        service.addValue(3);
        service.addValue(4, Duration.ofMillis(500));
        await().until(service::getValue, value -> value == 7);
    }
}
