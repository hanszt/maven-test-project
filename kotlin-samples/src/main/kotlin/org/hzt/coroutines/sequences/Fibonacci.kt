package org.hzt.coroutines.sequences

import java.util.concurrent.TimeUnit

val fibonacci = sequence {
    yield(1) // first Fibonacci number
    var cur = 1
    var next = 1
    while (true) {
        yield(next) // next Fibonacci number
        val tmp = cur + next
        cur = next
        next = tmp
    }
}

fun main() = fibonacci.take(10).onEach { TimeUnit.MILLISECONDS.sleep(250) }.forEach(::println)
