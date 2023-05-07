package org.hzt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class SimpleHtmlDslKtTest {

    @Test
    fun `test html dsl`() {
        val table = renderProductTable().replace("\" >", "\">")
        val fileName = "/html-dsl-result.html"
        val file = {}.javaClass.getResource(fileName)
            ?.let { File(it.file) } ?: error("Could not find '$fileName'")
        val expected = file.readLines()
            .drop(1)
            .joinToString("") { it.trim() }
        println(table)
        assertEquals(expected, table)
    }
}
