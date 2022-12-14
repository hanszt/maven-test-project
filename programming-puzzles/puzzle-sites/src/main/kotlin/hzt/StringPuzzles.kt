package hzt

import java.util.*
import java.util.stream.IntStream


/**
 * @see <a href="https://leetcode.com/problems/zigzag-conversion/">6. Zigzag Conversion</a>
 * 
 * Accepted 271 ms 37.8 MB
 */
fun String.zigZagConversion(numRows: Int): String {
    if (numRows == 1 || numRows >= length) return this
    val rows = zigZagConversion(numRows, this)
    val result = StringBuilder()
    rows.forEach(result::append)
    return result.toString()
}

private fun zigZagConversion(numRows: Int, s: String): Array<StringBuilder> {
    val rows = Array(numRows) { StringBuilder() }
    var curRow = 0
    var goingDown = false
    for (c in s.toCharArray()) {
        rows[curRow].append(c)
        if (curRow == 0 || curRow == rows.lastIndex) {
            goingDown = !goingDown
        }
        curRow += if (goingDown) 1 else -1
    }
    return rows
}

fun numDistinct(s: String, t: String): Int {
    val cache = Array(s.length) { IntArray(t.length) }
    for (ints in cache) {
        Arrays.fill(ints, -1)
    }
    return IntStream.range(0, s.length)
        .map { start: Int -> numDistinct(s, t, start, 0, cache) }
        .sum()
}

private fun numDistinct(s: String, t: String, startS: Int, startT: Int, cache: Array<IntArray>): Int {
    if (startS >= s.length || startT >= t.length || s[startS] != t[startT]) {
        return 0
    }
    if (startT == t.length - 1) {
        return 1
    }
    if (cache[startS][startT] != -1) {
        return cache[startS][startT]
    }
    var count = 0
    for (start in startS + 1 until s.length) {
        count += numDistinct(s, t, start, startT + 1, cache)
    }
    cache[startS][startT] = count
    return count
}

