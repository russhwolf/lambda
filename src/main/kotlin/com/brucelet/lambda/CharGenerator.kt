package com.brucelet.lambda

fun Parser.parseTypedChars() {
    parseLine("#def '0' = MAKE_CHAR forty_eight")
    parseLine("#def 'A' = MAKE_CHAR sixty_five")
    parseLine("#def 'a' = MAKE_CHAR ninety_seven")
    for (c in '1'..'9') {
        parseLine("#def '$c' = MAKE_CHAR (succ (value '${c - 1}'))")
    }
    for (c in 'B'..'Z') {
        parseLine("#def '$c' = MAKE_CHAR (succ (value '${c - 1}'))")
    }
    for (c in 'b'..'z') {
        parseLine("#def '$c' = MAKE_CHAR (succ (value '${c - 1}'))")
    }
}