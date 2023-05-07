package org.hzt.coroutines.recursion

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.math.max
import kotlin.test.assertEquals

/**
 * [DeepRecursiveFunction](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-deep-recursive-function/)
 */
class DeepRecursiveFunctionTest {

    private class Tree(val left: Tree? = null, val right: Tree? = null)
    private val deepTree = generateSequence(Tree()) { Tree(left = it) }.take(100_000).last()

    @Test
    fun `calculating the depth of a deep recursive tree with regular recursion, should result in a StackOverflowError`() {
        fun depth(tree: Tree?): Int = if (tree == null) 0 else max(depth(tree.left), depth(tree.right)) + 1
        assertThrows<StackOverflowError> { depth(deepTree) }
    }

    @Test
    fun `calculating the depth of a deep recursive tree with deep recursive function, should result in the depth`() {
        val depthByDeepRecursiveFunction = DeepRecursiveFunction<Tree?, Int> { tree ->
            if (tree == null) 0 else max(callRecursive(tree.left), callRecursive(tree.right)) + 1
        }
        val depth = depthByDeepRecursiveFunction.invoke(deepTree)
        assertEquals(100_000, depth)
    }
}