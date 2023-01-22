package org.hzt.coroutines.sequences

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.util.*
import kotlin.coroutines.*
import kotlin.experimental.ExperimentalTypeInference

/**
 * Source: https://github.com/Kotlin/coroutines-examples/blob/master/examples/suspendingSequence/suspendingSequence-example.kt
 *
 */
@OptIn(DelicateCoroutinesApi::class)
fun main() {
    val context = newSingleThreadContext("MyThread")
    runBlocking(context) {
        // asynchronously generate a number every 500 ms
        val seq = suspendingSequence(context) {
            log("Starting generator")
            for (i in 1..10) {
                log("Generator yields $i")
                yield(i)
                val generatorSleep = 500L
                log("Generator goes to sleep for $generatorSleep ms")
                delay(generatorSleep)
            }
            log("Generator is done")
        }
        // simulate async work by sleeping randomly
        val random = Random()
        // consume asynchronous sequence with a regular for loop
        for (value in seq) {
            log("Consumer got value = $value")
            val consumerSleep = random.nextInt(1000).toLong()
            log("Consumer goes to sleep for $consumerSleep ms")
            delay(consumerSleep)
        }
    }
}

interface SuspendingSequenceScope<in T> {
    suspend fun yield(value: T)
}

interface SuspendingSequence<out T> {
    operator fun iterator(): SuspendingIterator<T>
}

interface SuspendingIterator<out T> {
    suspend operator fun hasNext(): Boolean
    suspend operator fun next(): T
}

@OptIn(ExperimentalTypeInference::class)
fun <T> suspendingSequence(
    context: CoroutineContext = EmptyCoroutineContext,
    @BuilderInference block: suspend SuspendingSequenceScope<T>.() -> Unit
): SuspendingSequence<T> = object : SuspendingSequence<T> {
    override fun iterator(): SuspendingIterator<T> = suspendingIterator(context, block)
}

fun <T> suspendingIterator(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend SuspendingSequenceScope<T>.() -> Unit
): SuspendingIterator<T> =
    SuspendingIteratorCoroutine<T>(context).apply {
        nextStep = block.createCoroutine(receiver = this, completion = this)
    }

class SuspendingIteratorCoroutine<T>(
    override val context: CoroutineContext
) : SuspendingIterator<T>, SuspendingSequenceScope<T>, Continuation<Unit> {
    enum class State { INITIAL, COMPUTING_HAS_NEXT, COMPUTING_NEXT, COMPUTED, DONE }

    private var state: State = State.INITIAL
    private var nextValue: T? = null
    var nextStep: Continuation<Unit>? = null // null when sequence complete

    private var computeContinuation: Continuation<*>? = null

    private suspend fun computeHasNext(): Boolean = suspendCoroutine {
        state = State.COMPUTING_HAS_NEXT
        computeContinuation = it
        nextStep!!.resume(Unit)
    }

    private suspend fun computeNext(): T = suspendCoroutine {
        state = State.COMPUTING_NEXT
        computeContinuation = it
        nextStep!!.resume(Unit)
    }

    override suspend fun hasNext(): Boolean {
        return when (state) {
            State.INITIAL -> computeHasNext()
            State.COMPUTED -> true
            State.DONE -> false
            else -> throw IllegalStateException("Recursive dependency detected -- already computing next")
        }
    }

    override suspend fun next(): T {
        when (state) {
            State.INITIAL -> return computeNext()
            State.COMPUTED -> {
                state = State.INITIAL
                @Suppress("UNCHECKED_CAST")
                return nextValue as T
            }
            State.DONE -> throw NoSuchElementException()
            else -> throw IllegalStateException("Recursive dependency detected -- already computing next")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun resumeIterator(hasNext: Boolean) {
        when (state) {
            State.COMPUTING_HAS_NEXT -> {
                state = State.COMPUTED
                (computeContinuation as Continuation<Boolean>).resume(hasNext)
            }
            State.COMPUTING_NEXT -> {
                state = State.INITIAL
                (computeContinuation as Continuation<T>).resume(nextValue as T)
            }
            else -> throw IllegalStateException("Was not supposed to be computing next value. Spurious yield?")
        }
    }

    // Completion continuation implementation
    override fun resumeWith(result: Result<Unit>) {
        nextStep = null
        result
            .onSuccess {
                resumeIterator(false)
            }
            .onFailure { exception ->
                state = State.DONE
                computeContinuation!!.resumeWithException(exception)
            }
    }

    // Generator implementation
    override suspend fun yield(value: T): Unit = suspendCoroutine {
        nextValue = value
        nextStep = it
        resumeIterator(true)
    }
}
fun log(msg: String) = println("${Instant.now()} [${Thread.currentThread().name}] $msg")

