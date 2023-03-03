package org.hzt.coroutines.sequences

import org.hzt.coroutines.sequences.poc.SequenceScope
import org.hzt.coroutines.sequences.poc.sequence
import java.util.concurrent.TimeUnit
import kotlin.streams.asStream


/**
 * [Towers of Hanoi: A Complete Recursive Visualization](https://www.youtube.com/watch?v=rf6uf3jNjbo)
 *
 * A tower of hanoi implementation using a suspend function with Sequence scope.
 *
 * This allows recursive functions to emmit it's results to a sequence or an iterator
 */
suspend fun SequenceScope<String>.moveDisk(n: Int, from: Char, to: Char, aux: Char) {
    require(n >= 0) { "The nr of disks (n) can not be negative (n = $n)" }
    if (n == 0) return
    moveDisk(n - 1, from, aux, to)
    yield("Move disk %2d from rod $from to rod $to".format(n))
    moveDisk(n - 1, aux, to, from)
}

fun towerOfHanoiSequence(nrOfDiks: Int) = sequence { moveDisk(nrOfDiks, 'a', 'c', 'b') }
fun towerOfHanoiStream(nrOfDiks: Int) = towerOfHanoiSequence(nrOfDiks).asStream()

fun main() = towerOfHanoiSequence(3)
    .onEach { TimeUnit.MILLISECONDS.sleep(500) }
    .forEach(::println)

