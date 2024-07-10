package com.example

enum class TokensMeanings {
    Plus, Del, Minus, Mult, Percentage,
    Fractional, Num,
    CloseBrackets, OpenBrackets
}

internal object TokenConst {

    private const val TEMPLATE_COMBINATION = "nn_n+_n-_n÷_n×"
    const val BASE_OPERANDS = "+-"
    const val SECONDARY_OPERANDS="÷×"
    val incorrectCombination = mutableSetOf<String>().apply {
        BASE_OPERANDS.forEach {
            TEMPLATE_COMBINATION.replace("n", it.toString()).split("_").forEach { combine ->
                add(combine)
                add(combine.reversed())
            }
        }
    }.toSet()
    internal val mapTokens = mutableMapOf<TokensMeanings, Token>().apply {
        put(
            TokensMeanings.OpenBrackets,
            Token(define = "(", tokensMeanings = TokensMeanings.OpenBrackets, priority = -1)
        )
        put(
            TokensMeanings.CloseBrackets,
            Token(define = ")", tokensMeanings = TokensMeanings.CloseBrackets, priority = -1)
        )
        put(
            TokensMeanings.Del,
            Token(define = "÷", tokensMeanings = TokensMeanings.Del, priority = 3)
        )
        put(
            TokensMeanings.Fractional,
            Token(define = ".", tokensMeanings = TokensMeanings.Fractional, priority = -1)
        )
        put(
            TokensMeanings.Minus,
            Token(define = "-", tokensMeanings = TokensMeanings.Minus, priority = 2)
        )
        put(
            TokensMeanings.Mult,
            Token(define = "×", tokensMeanings = TokensMeanings.Mult, priority = 3)
        )
        put(
            TokensMeanings.Plus,
            Token(define = "+", tokensMeanings = TokensMeanings.Plus, priority = 2)
        )
        put(
            TokensMeanings.Percentage,
            Token(define = "%", tokensMeanings = TokensMeanings.Percentage, priority = 4)
        )
        put(
            TokensMeanings.Num,
            Token(define = "1234567890.E", tokensMeanings = TokensMeanings.Num, priority = -1)
        )
    }.toMap()
}

fun String.getOperandTokenMeanings(): TokensMeanings {
    return when (this) {
        TokensMeanings.Minus.getToken().define -> {
            TokensMeanings.Minus
        }

        TokensMeanings.Plus.getToken().define -> {
            TokensMeanings.Plus
        }

        TokensMeanings.Del.getToken().define -> {
            TokensMeanings.Del
        }

        TokensMeanings.Mult.getToken().define -> {
            TokensMeanings.Mult
        }

        TokensMeanings.Percentage.getToken().define->{
            TokensMeanings.Percentage
        }
        else -> {
            TokensMeanings.Num
        }
    }
}

internal fun TokensMeanings.getToken(): Token {

    return when (this) {
        TokensMeanings.OpenBrackets -> {
            TokenConst.mapTokens.getValue(this)
        }

        TokensMeanings.CloseBrackets -> {

            TokenConst.mapTokens.getValue(this)
        }

        TokensMeanings.Del -> {
            TokenConst.mapTokens.getValue(this)
        }


        TokensMeanings.Fractional -> {
            TokenConst.mapTokens.getValue(this)
        }

        TokensMeanings.Minus -> {

            TokenConst.mapTokens.getValue(this)
        }


        TokensMeanings.Mult -> {

            TokenConst.mapTokens.getValue(this)
        }

        TokensMeanings.Plus -> {
            TokenConst.mapTokens.getValue(this)
        }


        TokensMeanings.Percentage -> {
            TokenConst.mapTokens.getValue(this)
        }

        TokensMeanings.Num -> {
            TokenConst.mapTokens.getValue(this)
        }
    }
}


