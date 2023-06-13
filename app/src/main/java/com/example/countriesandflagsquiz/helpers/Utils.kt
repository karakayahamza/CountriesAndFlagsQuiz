package com.example.countriesandflagsquiz.helpers

fun randomFlags(): MutableSet<Int> {
    val randomNumbers = mutableSetOf<Int>()
    while (randomNumbers.size < 4) {
        randomNumbers.add((1..220).random())
    }
    return randomNumbers
}