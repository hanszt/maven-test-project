package org.hzt;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.hzt.utils.It;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LambdaLoggingTest {

    @Test
    @Disabled("Test does not pass because programmatically enabling the Debug level does not work")
    void testLogIfDebugEnabledOnlyExecutedIfDebugEnabled() {
        Timer<Void> timerDebugNotEnabled = Timer.timeAConsumer(
                LambdaLoggingTest::expensiveMessage,
                LambdaLogging::logIfDebugEnabled);

        final var context = LogManager.getContext(false);
        if (context instanceof LoggerContext ctx) {
            Configuration config = ctx.getConfiguration();
            LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
            loggerConfig.setLevel(Level.DEBUG);
            ctx.updateLoggers();  // This causes all Loggers to re-fetch information from their LoggerConfig.
        }
        Timer<Void> timerDebugEnabled = Timer.timeAConsumer(
                LambdaLoggingTest::expensiveMessage,
                LambdaLogging::logIfDebugEnabled);

        final var timeInMillisDebugNotEnabled = timerDebugNotEnabled.getDurationInMillis();
        final var timeInMillisDebugEnabled = timerDebugEnabled.getDurationInMillis();
        Timer<Void> timer = Timer.timeAConsumer("", It::println);
        println("timeInMillisDebugNotEnabled = " + timeInMillisDebugNotEnabled);
        println("timeInMillisDebugEnabled = " + timeInMillisDebugEnabled);
        println("timer.getTimeInMillis() = " + timer.getDurationInMillis());

        assertTrue(timeInMillisDebugNotEnabled < timeInMillisDebugEnabled);
    }

    private static String expensiveMessage() {
        final double pi = Streams.leibnizStream(100_000_000).sum() * 4;
        return String.valueOf(pi);
    }
}
