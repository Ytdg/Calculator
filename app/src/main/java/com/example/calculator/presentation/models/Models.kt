package com.example.calculator.presentation.models

import androidx.compose.runtime.Immutable
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

@Immutable
data class TextFieldExpressionState(
    val textFieldValue: TextFieldValue = TextFieldValue(text = "0", selection = TextRange(1)),
)
@Immutable
data class CalculatedExpressionState(
    val result:String=String(),
    val isError:Boolean=false
)
