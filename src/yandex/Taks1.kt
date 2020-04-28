package yandex

import java.util.*

fun searchAllChild(array: MutableList<Int>) {
    for (element in array) {
        if (element == 0) println(0)
        else println(getNext(array, element - 1))
    }
}
fun getNext(array: MutableList<Int>, positionInArray: Int, currentNumberPar: Int = 1): Int {
    if (array[positionInArray] == 0) return currentNumberPar
    return getNext(array, array[positionInArray] - 1, currentNumberPar + 1)
}
fun main() {
    val sc = Scanner(System.`in`)
    val n = sc.nextInt()
    val pi = mutableListOf<Int>()
    var i = 0
    while (i < n) {
        val nextInt = sc.nextInt()
        pi.add(nextInt)
        i++
    }
    searchAllChild(pi)
}