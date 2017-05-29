package com.brucelet.lambda

fun main(vararg args: kotlin.String) {
    println("(λf.(f λf.f) λs.(s s))".parseExpression().reduceOnce())
}