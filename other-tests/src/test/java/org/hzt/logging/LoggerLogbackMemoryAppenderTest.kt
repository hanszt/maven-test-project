package org.hzt.logging

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.core.Context
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

internal class LoggerLogbackMemoryAppenderTest {

    @Test
    fun `when testing only logging, then a memory appender should should be used to log the message`() {
        val msg = "This is the test message"
        val loggerName = BusinessWorker::class.java.name

        val memoryAppender = MemoryAppender().apply appender@{
            context = LoggerFactory.getILoggerFactory() as Context
            (LoggerFactory.getLogger(loggerName) as Logger).apply {
                level = Level.DEBUG
                addAppender(this@appender)
            }
            start()
        }
        BusinessWorker.generateLogs(msg)

        println(memoryAppender.loggedEvents)

        assertAll(
            { assertEquals(4, memoryAppender.countEventsForLogger(loggerName)) },
            { assertEquals(1, memoryAppender.search(msg, Level.INFO).size) },
            { assertFalse(memoryAppender.contains(msg, Level.TRACE)) }
        )
    }
}
