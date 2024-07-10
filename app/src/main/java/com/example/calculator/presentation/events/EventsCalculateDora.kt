package com.example.calculator.presentation.events

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.TextFieldValue
import com.example.calculator.domain.TokenInput


@Immutable
sealed class EventsCalculateDora {
    data class OnChangeTextFieldExpression(val textFieldValue: TextFieldValue) :
        EventsCalculateDora()

    data class OnChangeTextExpression(val tokenInput: TokenInput) : EventsCalculateDora()
    object ReturnExpression : EventsCalculateDora()
}
