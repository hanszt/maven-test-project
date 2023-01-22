package org.hzt.coroutines.channel

import kotlinx.coroutines.delay
import org.hzt.coroutines.mutex.Mutex

// https://tour.golang.org/concurrency/9

class SafeCounter {
    private val mutableMap = mutableMapOf<String, Int>()
    private val mutex = Mutex()

    suspend fun increment(key: String) {
        mutex.lock()
        try { mutableMap[key] = mutableMap.getOrDefault(key, 0) + 1 }
        finally { mutex.unlock() }
    }

    suspend fun get(key: String): Int? {
        mutex.lock()
        return try { mutableMap[key] }
        finally { mutex.unlock() }
    }
}

fun main() = mainBlocking {
    val safeCounter = SafeCounter()
    for (i in 0..999) {
        go { safeCounter.increment("somekey") } // 1000 concurrent coroutines
    }
    delay(1000)
    println("${safeCounter.get("somekey")}")
}
