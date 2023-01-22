package org.hzt

@JvmName("listOfLongValues")
fun listOfValues(values: List<Long>): List<Long> = values

@JvmName("listOfIntValues")
fun listOfValues(values: List<Int>): List<Int> = values

@JvmName("listOfValues")
fun <T> listOfValues(values: List<T>): List<T> = values

fun <R> callBackOverload(callback: () -> R): R = callback()

fun callBackOverload(callback: () -> Long): Long = callback()
