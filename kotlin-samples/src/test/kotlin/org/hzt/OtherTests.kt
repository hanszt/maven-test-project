package org.hzt

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class OtherTests {

    @Test
    fun testIncrementChars() {
        var counter = 'A'.code
        var character = 'A'
        while (character <= 'z') {
            println("character = $character")
            val nr = character.code
            println("asciNr = $nr")
            println("nr to char: ${nr.toChar()}")
            counter++
            character++
        }
        counter - 1 shouldBe 'z'.code
    }
}
