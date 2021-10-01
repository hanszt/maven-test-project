package com.dnb;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LambdaLoggingTest {

    @Test
    @Disabled("Test does not pass because programmatically enabling the Debug level does not work")
    void testLogIfDebugEnabledOnlyExecutedIfDebugEnabled() {
        Timer<Void> timerDebugNotEnabled = Timer.timeAConsumer(
                LambdaLoggingTest::expensiveMessage,
                LambdaLogging::logIfDebugEnabled);

        final var context = LogManager.getContext(false);
        if (context instanceof LoggerContext) {
            LoggerContext ctx = (LoggerContext) context;
            Configuration config = ctx.getConfiguration();
            LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
            loggerConfig.setLevel(Level.DEBUG);
            ctx.updateLoggers();  // This causes all Loggers to refetch information from their LoggerConfig.
        }
        Timer<Void> timerDebugEnabled = Timer.timeAConsumer(
                LambdaLoggingTest::expensiveMessage,
                LambdaLogging::logIfDebugEnabled);

        final var timeInMillisDebugNotEnabled = timerDebugNotEnabled.getTimeInMillis();
        final var timeInMillisDebugEnabled = timerDebugEnabled.getTimeInMillis();
        Timer<Void> timer = Timer.timeAConsumer("", System.out::println);
        System.out.println("timeInMillisDebugNotEnabled = " + timeInMillisDebugNotEnabled);
        System.out.println("timeInMillisDebugEnabled = " + timeInMillisDebugEnabled);
        System.out.println("timer.getTimeInMillis() = " + timer.getTimeInMillis());
        assertTrue(timeInMillisDebugNotEnabled < timeInMillisDebugEnabled);
    }

    private static String expensiveMessage() {
        return String.valueOf(StreamsSample.calculatePiAsDouble(100_000_000, false));
    }
}
