package com.example.calculator.presentation.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.calculator.domain.ResultCalculate
import com.example.calculator.domain.TokensMeaningsInput
import com.example.calculator.domain.usecase.UseCases
import com.example.calculator.presentation.events.EventsCalculateDora
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelCalculateDora @Inject constructor(private val useCases: UseCases) : ViewModel() {
    private val _stateExpression: MutableState<TextFieldExpressionState> =
        mutableStateOf(TextFieldExpressionState())
    val stateExpression: State<TextFieldExpressionState> = _stateExpression
    private val _stateCalculatedExpression: MutableState<CalculatedExpressionState> =
        mutableStateOf(
            CalculatedExpressionState()
        )
    val stateCalculatedExpression: State<CalculatedExpressionState> = _stateCalculatedExpression
    fun onEvent(event: EventsCalculateDora) {

        when (event) {
            is EventsCalculateDora.ReturnExpression -> {
                _stateCalculatedExpression.value = CalculatedExpressionState()
            }

            is EventsCalculateDora.OnChangeTextFieldExpression -> {
                _stateExpression.value.let {
                    _stateExpression.value = it.copy(textFieldValue = event.textFieldValue)
                }
            }

            is EventsCalculateDora.OnChangeTextExpression -> {
                if (event.tokenInput.tokensMeaningsInput == TokensMeaningsInput.Reset) {
                    _stateCalculatedExpression.value = CalculatedExpressionState()
                }
                if (stateCalculatedExpression.value.result.isEmpty() || event.tokenInput.tokensMeaningsInput == TokensMeaningsInput.Reset) {
                    if (event.tokenInput.tokensMeaningsInput == TokensMeaningsInput.Equals) {
                        with(_stateExpression.value.textFieldValue) {
                            when (val result = useCases.useCaseCalculate(expression = text)) {
                                is ResultCalculate.Error -> {
                                    _stateCalculatedExpression.value = CalculatedExpressionState(
                                        result = result.message,
                                        isError = true
                                    )
                                }

                                is ResultCalculate.Successfully -> {
                                    _stateCalculatedExpression.value =
                                        CalculatedExpressionState(result = result.value)
                                }
                            }
                        }
                    } else {
                        _stateExpression.value.let {
                            _stateExpression.value = it.copy(
                                textFieldValue = useCases.useCaseInputExpression(
                                    tokenInput = event.tokenInput,
                                    it.textFieldValue
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}