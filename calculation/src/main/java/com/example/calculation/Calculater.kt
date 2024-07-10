package com.example.calculation

import com.example.Token
import com.example.TokenConst
import com.example.TokensMeanings
import com.example.getToken
import com.example.toToken

//All exceptions must be described
//the first parameter has a string expression containing separator
class Calculater(listSymbols: List<String>) {

    private var listSymbolExpression = listSymbols

    companion object {
        private fun isValidateOperand(expression: String): Boolean {
            TokenConst.incorrectCombination.forEach {
                if (expression.contains(it)) {
                    return false
                }
            }
            return true
        }
    }


    init {
        if (!isValidateOperand(listSymbolExpression.joinToString(separator = ""))) {
            throw IllegalArgumentException("Incorrect operand placement")
        }
    }

    fun CalculateExpression(): String {

        val indexBrackets = getIndexBrackets(listSymbolExpression)
        val d =
            listSymbolExpression.getExpressionNoBrackets(indexBrackets).removeFirstExtraOperators()
                .join()
        val result = CalculateNoBrackets(expressionWithoutBrackets = d)
        listSymbolExpression = listSymbolExpression.replaceExpression(
            indices = indexBrackets,
            resultCalculated = result
        )
        if (listSymbolExpression.size == 1) {
            return listSymbolExpression.first().toList().map { it.toString() }
                .removeFirstExtraOperators().joinToString(separator = "")
        }
        return CalculateExpression()
    }

    private fun CalculateNoBrackets(expressionWithoutBrackets: List<String>): String {
        val result = expressionWithoutBrackets.map { it.toToken() }
        val indexOperand = result.maxByOrNull { it.priority }?.takeIf { it.priority != -1 }
            ?.let { result.indexOf(it) }
        if (indexOperand == null) {
            return result.first().define
        }
        var res = ""
        try {
            res = Calculate.Expression(
                firstToken = result[indexOperand - 1],
                secondToken = if (result[indexOperand].tokensMeanings == TokensMeanings.Percentage) "1".toToken() else result[indexOperand + 1],
                operand = result[indexOperand].tokensMeanings
            )
        } catch (ex: Exception) {
            throw ex
        }

        return CalculateNoBrackets(
            expressionWithoutBrackets = result.map { it.define }.replaceExpression(
                indices = Pair(
                    first = indexOperand - 1,
                    second = if (result[indexOperand].tokensMeanings == TokensMeanings.Percentage) indexOperand else indexOperand + 1
                ), resultCalculated = res
            )
        )
    }

    private fun List<String>.join(): List<String> {

        var newList = this.toMutableList()
        if (size > 1) {
            newList = mutableListOf()
            var numStr = first().let {
                if (it.toDoubleOrNull() != null || TokenConst.BASE_OPERANDS.contains(
                        it
                    )
                ) {
                    it
                } else {
                    newList.add(it)
                    ""
                }
            }
            var countConsecutiveOperatore = 0
            for (s in 1..<size) {
                if (get(s).toDoubleOrNull() != null || (TokenConst.BASE_OPERANDS.contains(
                        get(s)
                    ) && countConsecutiveOperatore == 1)
                ) {
                    numStr += get(s)
                    countConsecutiveOperatore = 0
                } else {
                    if (numStr.isNotEmpty()) {
                        newList.add(numStr)
                    }
                    numStr = ""
                    newList.add(get(s))
                    if (TokenConst.BASE_OPERANDS.contains(get(s)) || TokenConst.SECONDARY_OPERANDS.contains(
                            get(s)
                        )
                    ) {
                        countConsecutiveOperatore++
                    }
                }
            }
            if (numStr.isNotEmpty()) {
                newList.add(numStr)
            }
        }
        return newList
    }


    private fun List<String>.getExpressionNoBrackets(indices: Pair<Int, Int>): List<String> {

        if (indices.first * indices.second == 1) {
            return this
        }
        if (indices.first != -1 && indices.second != -1) {

            return this.subList(indices.first + 1, indices.second)
        }
        throw IllegalArgumentException(
            "\n" +
                    "incorrect placement of brackets"
        )
    }

    //    Replacing an expression without parentheses with a calculated one
    private fun List<String>.replaceExpression(
        indices: Pair<Int, Int>,
        resultCalculated: String
    ): List<String> {


        if (indices.first * indices.second == 1) {
            return listOf(resultCalculated)
        }

        if (indices.first != -1 && indices.second != -1) {
            val list = this.toMutableList()
            list.add(resultCalculated)
            repeat(indices.second - indices.first + 1) {
                list.removeAt(indices.first)
            }
            list.removeLast()
            list.add(indices.first, resultCalculated)

            return list
        }
        throw IllegalArgumentException(
            "\n" +
                    "incorrect placement token"
        )
    }


    //    get indexes of closed and open parentheses
    private fun getIndexBrackets(listSymbols: List<String>): Pair<Int, Int> {

        if (listSymbols.count { it == TokensMeanings.OpenBrackets.getToken().define } == listSymbols.count { it == TokensMeanings.CloseBrackets.getToken().define }) {
            val indexOpenBracket =
                listSymbols.indexOfLast { it == TokensMeanings.OpenBrackets.getToken().define }
            val indexCloseBracket = indexOpenBracket.takeIf { it != -1 }?.let {
                listSymbols.subList(it, listSymbols.size)
                    .indexOfFirst { it == TokensMeanings.CloseBrackets.getToken().define }.let {
                        if (it == -1) {
                            it
                        } else {
                            it + indexOpenBracket
                        }
                    }
            } ?: -1
            return Pair(indexOpenBracket, indexCloseBracket)
        } else {
            throw IllegalArgumentException(
                "\n" +
                        "incorrect number brackets"
            )
        }
    }

}

internal object Calculate {
    fun Expression(firstToken: Token, secondToken: Token, operand: TokensMeanings): String {

//        there will be other operators
        return when (operand) {
            TokensMeanings.Del -> {
                return firstToken / secondToken
            }

            TokensMeanings.Mult -> {
                return firstToken * secondToken
            }

            TokensMeanings.Minus -> {
                return firstToken - secondToken
            }

            TokensMeanings.Plus -> {
                return firstToken + secondToken
            }

            TokensMeanings.Percentage -> {
                firstToken % secondToken
            }

            else -> {
                throw IllegalArgumentException("not yet implemented")
            }
        }

    }

    private operator fun Token.plus(token: Token): String {
        return (define.getDouble() + token.define.getDouble()).toString()
    }

    private operator fun Token.minus(token: Token): String {
        return (define.getDouble() - token.define.getDouble()).toString()
    }

    private operator fun Token.times(token: Token): String {
        return (define.getDouble() * token.define.getDouble()).toString()
    }

    private operator fun Token.div(token: Token): String {
        if (token.define.getDouble() == 0.0) {
            throw IllegalArgumentException("by zero")
        }
        return (this.define.getDouble() / token.define.getDouble()).toString()
    }

    private operator fun Token.rem(token: Token): String {
        return ((this.define.getDouble() / 100) * token.define.getDouble()).toString()
    }

    private fun String.getDouble(): Double {
        val token =
            toList().map { it.toString() }.removeFirstExtraOperators().joinToString(separator = "")
        token.toDoubleOrNull().takeIf { it != null }?.let { return it }
        throw IllegalArgumentException(
            "\n" +
                    "incorrect entry"
        )
    }
}


internal fun List<String>.removeFirstExtraOperators(): List<String> {
    val newList = this.toMutableList()
    newList.firstOrNull()?.takeIf { it == TokensMeanings.Plus.getToken().define }?.let {
        newList.removeFirst()
    }
    newList.takeIf { it.isNotEmpty() }?.let {
        if (TokenConst.SECONDARY_OPERANDS.contains(it.first())) {
            throw IllegalArgumentException("incorrect position operand")
        }
    }

    val subListExtra = takeIf { it.size >= 2 }?.subList(0, 2)
        ?.takeIf {
            TokenConst.BASE_OPERANDS.contains(it.first()) && TokenConst.BASE_OPERANDS.contains(
                it.last()
            )
        }?.joinToString("")
    subListExtra?.let {
        val extraCombine =
            TokensMeanings.Minus.getToken().define + TokensMeanings.Plus.getToken().define
        when (it) {
            TokensMeanings.Minus.getToken().define.repeat(2) -> {
                repeat(2) {
                    newList.removeFirst()
                }
            }

            extraCombine -> {
                newList.removeAt(1)
            }

            extraCombine.reversed() -> {
                newList.removeAt(0)
            }

            else -> {
                throw IllegalArgumentException(
                    "\n" +
                            "incorrect position operand"
                )
            }

        }

    }
    return newList
}


