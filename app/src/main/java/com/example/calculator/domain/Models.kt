package com.example.calculator.domain

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.toImmutableList
import java.util.UUID
import kotlin.Result

sealed class ResultCalculate<T> {
    data class Successfully<T>(val value: T) : ResultCalculate<T>()
    data class Error<T>(val message: T) : ResultCalculate<T>()
}

@Immutable
data class TokenInput(
    val tokensMeaningsInput: TokensMeaningsInput,
    val define: String,
    val color: Color = Color.White,
    val variableId: String = UUID.randomUUID().toString()
)

object TokensCollection {

    val globalTokens = listOf(
        TokensMeaningsInput.Reset.getTokenInput(),
        TokensMeaningsInput.Brackets.getTokenInput(),
        TokensMeaningsInput.Percentage.getTokenInput(),
        TokensMeaningsInput.Remove.getTokenInput()
    ).toImmutableList()

    val arithmetic = listOf(
        TokensMeaningsInput.Plus.getTokenInput(),
        TokensMeaningsInput.Minus.getTokenInput(),
        TokensMeaningsInput.Mult.getTokenInput(),
        TokensMeaningsInput.Del.getTokenInput()
    ).toImmutableList()

    val numbers = mutableListOf<TokenInput>(
    ).apply {
        "789456123${TokensMeaningsInput.Fractional.getTokenInput().define}0".forEach {
            add(
                TokenInput(
                    tokensMeaningsInput = if (it.toString() == TokensMeaningsInput.Fractional.getTokenInput().define) TokensMeaningsInput.Fractional else TokensMeaningsInput.Num,
                    define = it.toString()
                )
            )
        }
        add(
            TokensMeaningsInput.Equals.getTokenInput()
        )
    }.toImmutableList()
}


