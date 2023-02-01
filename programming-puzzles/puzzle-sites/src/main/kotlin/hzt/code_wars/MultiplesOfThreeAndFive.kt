package hzt.code_wars

/**
 * https://www.codewars.com/kata/514b92a657cdc65150000006/train/kotlin
 *
 * @param number
 * @return
 */
fun solution(number: Int): Int = if (number < 0) 0 else (3 until number)
    .filter { it % 3 == 0 || it % 5 == 0 }
    .sum()
