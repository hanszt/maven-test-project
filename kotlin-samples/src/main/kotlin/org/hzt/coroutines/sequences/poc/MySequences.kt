package org.hzt.coroutines.sequences.poc

import kotlin.coroutines.*

/**
 * Sequence scope
 *
 * @see <a href="https://github.com/Kotlin/coroutines-examples/blob/master/examples/sequence/sequence.kt">SequenceScope</a>
 *
 * @param T the type of the sequence scope
 * @constructor Create empty Sequence scope
 */
@RestrictsSuspension
interface SequenceScope<T> {
    suspend fun yield(value: T)
}

fun <T> sequence(block: suspend SequenceScope<T>.() -> Unit): Sequence<T> = Sequence { iterator(block) }

fun <T> iterator(block: suspend SequenceScope<T>.() -> Unit): Iterator<T> = SequenceCoroutine<T>()
    .apply { nextStep = block.createCoroutine(receiver = this, completion = this) }

private class SequenceCoroutine<T> : AbstractIterator<T>(), SequenceScope<T>, Continuation<Unit> {
    lateinit var nextStep: Continuation<Unit>

    // AbstractIterator implementation
    override fun computeNext() {
        nextStep.resume(Unit)
    }

    // Completion continuation implementation
    override val context: CoroutineContext get() = EmptyCoroutineContext

    override fun resumeWith(result: Result<Unit>) {
        result.getOrThrow() // bail out on error
        done()
    }

    // Generator implementation
    override suspend fun yield(value: T) {
        setNext(value)
        return suspendCoroutine { continuation -> nextStep = continuation }
    }
}
