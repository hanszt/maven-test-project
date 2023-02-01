package org.hzt.coroutines.sequences

import org.hzt.coroutines.sequences.poc.SequenceScope
import org.hzt.coroutines.sequences.poc.sequence
import java.util.concurrent.TimeUnit


/**
 * A tower of hanoi implementation using a suspend function with Sequence scope.
 *
 * This allows recursive functions to emmit it's results to a sequence or an iterator
 */
suspend fun SequenceScope<String>.moveDisk(diskNumber: Int, fromRod: Char, targetRod: Char, auxRod: Char) {
    if (diskNumber == 1) {
        yield("Move disk %2d from rod %c to rod %c".format(diskNumber, fromRod, targetRod))
        return
    }
    moveDisk(diskNumber - 1, fromRod, auxRod, targetRod)
    yield("Move disk %2d from rod %c to rod %c".format(diskNumber, fromRod, targetRod))
    moveDisk(diskNumber - 1, auxRod, targetRod, fromRod)
}

fun main() = sequence { moveDisk(3, 'a', 'c', 'b') }
    .onEach { TimeUnit.MILLISECONDS.sleep(500) }
    .forEach(::println)

