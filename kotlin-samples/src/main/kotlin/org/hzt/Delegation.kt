package org.hzt

/**
 * Base The interface to implement
 *
 * [Delegation](https://kotlinlang.org/docs/delegation.html)
 */
interface Base {
    val message: String
    fun messageByMethod(): String
}

class BaseImpl(x: Int) : Base {
    override val message = "BaseImpl: x = $x"
    override fun messageByMethod(): String = message
}

class Derived(base: Base) : Base by base {
    // This property is not accessed from base's implementation of `message()`
    override val message = "Message of Derived"
}

fun main() {
    val baseImpl = BaseImpl(10)
    val derived = Derived(baseImpl)
    println(derived.messageByMethod())
    println(derived.message)
}
