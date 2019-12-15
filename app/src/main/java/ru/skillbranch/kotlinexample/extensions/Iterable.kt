package ru.skillbranch.kotlinexample.extensions

fun <T> List<T>.dropLastUntil(predicate: (T) -> Boolean): List<T> {
    val resultList = mutableListOf<T>()
    for (i in 0 until size) {
        if (predicate(this[i])) break
        resultList.add(this[i])
    }
    return resultList
}