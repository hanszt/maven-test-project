package hzt


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

