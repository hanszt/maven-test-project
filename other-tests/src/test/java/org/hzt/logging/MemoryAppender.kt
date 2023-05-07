package org.hzt.logging

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import java.util.function.Predicate
import java.util.stream.Collectors

internal class MemoryAppender : ListAppender<ILoggingEvent>() {
    fun contains(string: CharSequence, level: Level): Boolean =
        list.any{ it.toString().contains(string) && it?.level == level }

    fun countEventsForLogger(loggerName: CharSequence): Int = list.count { event ->
        event?.loggerName?.contains(loggerName) ?: false
    }

    fun search(string: String, level: Level): List<ILoggingEvent> =
        list.filter { it.toString().contains(string) && it.level == level }

    val loggedEvents: List<ILoggingEvent>
        get() = list.toList()
}
