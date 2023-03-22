package org.hzt

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.test.assertEquals

class DelegationKtTest {

    @Test
    fun `test implementation by delegation`() {
        val baseImpl = BaseImpl(10)
        val derived = Derived(baseImpl)

        assertAll(
            { assertEquals("BaseImpl: x = 10", derived.message()) },
            { assertEquals("Message of Derived", derived.message) }
        )
    }
}
