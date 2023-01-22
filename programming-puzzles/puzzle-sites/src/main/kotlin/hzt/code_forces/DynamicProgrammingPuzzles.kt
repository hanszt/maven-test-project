package hzt.code_forces

/**
 * Codeforces problem 1552F
 *
 * [F. Telepanting](https://codeforces.com/problemset/problem/1552/F)
 * [Teleporting Ants & Dynamic Programming](https://www.youtube.com/watch?v=_DaTsI42Wvo)
 *
 * Time complexity: O(n log(n))
 */
fun telepanting(x: IntArray, y: IntArray, s: IntArray): Int {
    require(x.size == y.size && y.size == s.size)
    var ans = 0
    val ps = IntArray(x.size + 1)
    for (i in x.indices) {
        val dist = x[i] - y[i]
        val j = x.bisect(y[i])
        val cost = ps[i] - ps[j]
        val dpi = cost + dist
        ps[i + 1] = ps[i] + dpi
        ans += s[i] * dpi
    }
    val end = x.last() + 1
    return (ans + end) % 998_244_353
}


/**
 * Bisect locates the proper insertion index to maintain order. It is an adaption of binary search
 *
 * @return the index to insert the given key at to maintain order
 */
fun IntArray.bisect(key: Int, loInit: Int = 0, hiInit: Int = size): Int {
    if (isEmpty()) return 0
    if (key < this[loInit]) return loInit
    if (key > this[hiInit - 1]) return hiInit
    var lo = loInit
    var hi = hiInit
    while (true) {
        if (lo + 1 == hi) return lo + 1
        //using bitshift right to divide by 2
        val mid = (hi + lo) ushr 1
        if (key < this[mid]) hi = mid
        else lo = mid
    }
}
