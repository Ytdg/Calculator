package com.example.calculator.domain

import com.example.calculator.presentation.ui.baseColor

enum class TokensMeaningsInput {
    Plus, Del, Minus, Mult, Percentage,
    Reset, Brackets,
    Fractional, Remove, Num, Equals,
    CloseBrackets, OpenBrackets
}

object TokensConst {
    const val SeparatorDoubleOperator = "$"
    const val arithmeticOperand = "-+÷×"
}

/*doubles operand have id,$id*/

/*n-operand -  Del,Mult,Plus,Percentage */



fun String.getTokenMeanings(): TokensMeaningsInput {
    return when (this) {
        "(" -> {
            TokensMeaningsInput.OpenBrackets
        }

        ")" -> {
            TokensMeaningsInput.CloseBrackets
        }

        "÷" -> {
            TokensMeaningsInput.Del
        }

        "=" -> {
            TokensMeaningsInput.Equals
        }

        "⌫" -> {
            TokensMeaningsInput.Remove
        }

        "." -> {
            TokensMeaningsInput.Fractional
        }

        "-" -> {
            TokensMeaningsInput.Minus
        }

        "C" -> {
            TokensMeaningsInput.Reset
        }

        "×" -> {
            TokensMeaningsInput.Mult
        }

        "+" -> {
            TokensMeaningsInput.Plus
        }

        "(  )" -> {
            TokensMeaningsInput.Brackets
        }

        "%" -> {
            TokensMeaningsInput.Percentage
        }

        else -> {
            if (this.toDoubleOrNull() != null) {
                TokensMeaningsInput.Num
            } else {
                throw IllegalArgumentException("incorrect defineToken")
            }
        }
    }
}

fun TokensMeaningsInput.getTokenInput(singleDefineToken: String? = null): TokenInput {

    singleDefineToken?.let {
        return TokenInput(tokensMeaningsInput = this, define = singleDefineToken)
    }

    val tokenInputDefine = when (this) {
        TokensMeaningsInput.OpenBrackets -> {
            TokenInput(tokensMeaningsInput = this, define = "(")
        }

        TokensMeaningsInput.CloseBrackets -> {
            TokenInput(tokensMeaningsInput = this, define = ")")
        }

        TokensMeaningsInput.Num -> {
            TokenInput(tokensMeaningsInput = this, define = "7894561230")

        }

        TokensMeaningsInput.Del -> {
            TokenInput(tokensMeaningsInput = this, define = "÷")
        }

        TokensMeaningsInput.Equals -> {
            TokenInput(tokensMeaningsInput = this, define = "=", color = baseColor)
        }

        TokensMeaningsInput.Remove -> {
            TokenInput(tokensMeaningsInput = this, define = "⌫")
        }

        TokensMeaningsInput.Fractional -> {
            TokenInput(tokensMeaningsInput = this, define = ".")
        }

        TokensMeaningsInput.Minus -> {
            TokenInput(tokensMeaningsInput = this, define = "-")

        }

        TokensMeaningsInput.Reset -> {
            TokenInput(tokensMeaningsInput = this, define = "C")
        }

        TokensMeaningsInput.Mult -> {
            TokenInput(tokensMeaningsInput = this, define = "×")
        }

        TokensMeaningsInput.Plus -> {
            TokenInput(tokensMeaningsInput = this, define = "+")
        }

        TokensMeaningsInput.Brackets -> {
            TokenInput(tokensMeaningsInput = this, define = "(  )")
        }


        TokensMeaningsInput.Percentage -> {
            TokenInput(tokensMeaningsInput = this, define = "%")
        }
    }
    return tokenInputDefine
}
