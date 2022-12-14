package org.hzt


fun splitNonRegex(input: String, delim: String): List<String> {
    return if (delim.isNotEmpty()) {
        splitNonRegexNonEmptyDelimTailRecursive(input, delim)
    } else input.map(Char::toString)
}


private tailrec fun splitNonRegexNonEmptyDelimTailRecursive(
    input: String,
    delim: String,
    l: MutableList<String> = ArrayList(2),
    offset: Int = 0
): List<String> {
    val index = input.indexOf(delim, offset)
    if (index == -1) {
        return l.plus(input.substring(offset))
    }
    l.add(input.substring(offset, index))
    return splitNonRegexNonEmptyDelimTailRecursive(input, delim, l, index + delim.length)
}
