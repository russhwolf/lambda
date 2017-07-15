package com.brucelet.lambda

fun Parser.parseUntypedNumbers() {
    parseLine("#def ${makeNumberString(0)} = identity")
    for (i in 1..128) {
        parseLine("#def ${makeNumberString(i)} = λs.(s false ${makeNumberString(i - 1)})")
    }
}

fun Parser.parseTypedNumbers() {
    for (i in 0..128) {
        parseLine("#def $i = λs.(s numb_type ${makeNumberString(i)})")
    }
}

fun makeNumberString(number: Int): String {
    if (number >= 1000) {
        throw IllegalArgumentException("Cannot process numbers larger than 1000")
    }
    if (number < 0) {
        throw IllegalArgumentException("Cannot process negative numbers")
    }
    val ones = number % 10
    val tens = (number / 10) % 10
    val hundreds = (number / 100) % 10

    return when {
        hundreds == 0 -> when {
            tens == 0 -> onesString(ones)
            ones == 0 -> tensString(tens)
            tens == 1 -> teensString(number)
            else -> "${tensString(tens)}_${onesString(ones)}"
        }
        tens == 0 && ones == 0 -> hundredsString(hundreds)
        else -> "${hundredsString(hundreds)}_and_${makeNumberString(number % 100)}"
    }
}

private fun onesString(ones: Int): String = when(ones) {
    0 -> "zero"
    1 -> "one"
    2 -> "two"
    3 -> "three"
    4 -> "four"
    5 -> "five"
    6 -> "six"
    7 -> "seven"
    8 -> "eight"
    9 -> "nine"
    else -> throw IllegalArgumentException()
}

private fun tensString(tens: Int): String = when(tens) {
    1 -> "ten"
    2 -> "twenty"
    3 -> "thirty"
    4 -> "forty"
    5 -> "fifty"
    6 -> "sixty"
    7 -> "seventy"
    8 -> "eighty"
    9 -> "ninety"
    else -> throw IllegalArgumentException()
}

private fun teensString(number: Int): String = when (number) {
    11 -> "eleven"
    12 -> "twelve"
    13 -> "thirteen"
    14 -> "fourteen"
    15 -> "fifteen"
    16 -> "sixteen"
    17 -> "seventeen"
    18 -> "eighteen"
    19 -> "nineteen"
    else -> throw IllegalArgumentException()
}

private fun hundredsString(hundreds: Int): String = "${onesString(hundreds)}_hundred"


