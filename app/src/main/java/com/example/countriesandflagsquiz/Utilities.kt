package com.example.countriesandflagsquiz


fun randomFlags(): MutableSet<Int> {
    val randomNumbers = mutableSetOf<Int>()
    while (randomNumbers.size < 4) {
        randomNumbers.add((1..220).random())
    }
    //println(randomNumbers)
    return randomNumbers
}
