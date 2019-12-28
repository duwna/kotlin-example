package ru.skillbranch.kotlinexample.extensions

fun <T> List<T>.dropLastUntil(predicate: (T) -> Boolean): List<T> {
    val resultList = mutableListOf<T>()
    for (item in this) {
        if (predicate(item)) break
        resultList.add(item)
    }
    return resultList
}