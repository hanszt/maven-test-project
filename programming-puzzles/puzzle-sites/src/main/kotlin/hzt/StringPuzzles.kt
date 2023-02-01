package hzt


/**
 * @see <a href="https://leetcode.com/problems/zigzag-conversion/">6. Zigzag Conversion</a>
 * 
 * Accepted 271 ms 37.8 MB
 */
fun String.zigZagConversion(numRows: Int): String {
    if (numRows == 1 || numRows >= length) return this
    return zigZagConversion(numRows, this).joinToString("")
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
    val cache = Array(s.length) { IntArray(t.length) { -1 } }
    return s.indices.sumOf { cache.numDistinct(s, t, it, 0) }
}

private fun Array<IntArray>.numDistinct(s: String, t: String, startS: Int, startT: Int): Int {
    if (startS >= s.length || startT >= t.length || s[startS] != t[startT]) return 0
    if (startT == t.length - 1) return 1
    if (this[startS][startT] != -1) return this[startS][startT]

    val count = (startS + 1 until s.length).sumOf { numDistinct(s, t, it, startT + 1) }
    this[startS][startT] = count
    return count
}

