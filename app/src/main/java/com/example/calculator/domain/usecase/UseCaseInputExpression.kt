package com.example.calculator.domain.usecase

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.example.calculation.Calculater
import com.example.calculator.domain.TokenInput
import com.example.calculator.domain.TokensConst
import com.example.calculator.domain.TokensMeaningsInput
import com.example.calculator.domain.getTokenInput
import com.example.calculator.domain.getTokenMeanings
import java.util.UUID
import javax.inject.Inject
import kotlin.properties.Delegates

/*RULE INPUT->
1) operand before number only  "-" if there is no number in front of it
2)no double operand
3)change operands of the same order
4)all operands except - have a position after the number
5)after the parentheses any operands can be used
6) brackets have opposite id
 */







class UseCaseInputExpression @Inject constructor() {


    private lateinit var newTextFieldValue: TextFieldValue

    private var listTokenInput: MutableList<TokenInput> = mutableListOf()

    private var positionFocusToken by Delegates.notNull<Int>()

    operator fun invoke(tokenInput: TokenInput, textFieldValue: TextFieldValue): TextFieldValue {

        preparation(textFieldValue)

        with(newTextFieldValue) {
            when (tokenInput.tokensMeaningsInput) {

                TokensMeaningsInput.Plus -> {

                    useGeneralPositionOperator(tokenInput)

                }

                TokensMeaningsInput.Del -> {
                    useGeneralPositionOperator(tokenInput)
                }

                TokensMeaningsInput.Num -> {
                    listTokenInput.insertToken(
                        tokenInput.copy(variableId = UUID.randomUUID().toString()),
                        positionFocusToken
                    )
                    saveTextField(TextRange(selection.start + 1))
                }

                TokensMeaningsInput.Mult -> {
                    useGeneralPositionOperator(tokenInput)
                }

                TokensMeaningsInput.Minus -> {
//                    may have a position at the beginning
                    if (positionFocusToken == 0) {
                        if (TokensConst.arithmeticOperand.contains(listTokenInput[positionFocusToken].define)) {
                            listTokenInput[positionFocusToken] =
                                tokenInput.copy(variableId = UUID.randomUUID().toString())
                        } else {
                            listTokenInput.insertToken(
                                tokenInput = tokenInput.copy(
                                    variableId = UUID.randomUUID().toString()
                                ), position = positionFocusToken, removeZero = true
                            )
                        }
                        saveTextField(textRangeSelection = TextRange(positionFocusToken + 1))
                        return@with
                    }
                    useGeneralPositionOperator(tokenInput)

                }

                TokensMeaningsInput.Fractional -> {
                    var useFractional = true
//                    does fractional already exist
                    val check = { intProgress: IntProgression ->
                        for (s in intProgress) {
                            if (listTokenInput[s].tokensMeaningsInput != TokensMeaningsInput.Fractional && listTokenInput[s].tokensMeaningsInput != TokensMeaningsInput.Num) {
                                break
                            }
                            if (listTokenInput[s].tokensMeaningsInput == TokensMeaningsInput.Fractional) {
                                useFractional = false;break
                            }
                        }
                    }
                    check(positionFocusToken - 1 downTo 0)
                    check(positionFocusToken..<listTokenInput.size)
                    if (useFractional) {
                        if (positionFocusToken != 0 && listTokenInput[positionFocusToken - 1].tokensMeaningsInput == TokensMeaningsInput.Num) {
                            listTokenInput.insertToken(
                                tokenInput = tokenInput.copy(
                                    variableId = UUID.randomUUID().toString()
                                ), positionFocusToken, removeZero = false
                            )
                            saveTextField(textRangeSelection = TextRange(positionFocusToken + 1))
                        }

                    }

                }


                TokensMeaningsInput.Remove -> {
                    if (positionFocusToken != 0 && listTokenInput.isNotEmpty()) {
                        listTokenInput.removeAt(positionFocusToken - 1)
                        saveTextField(TextRange(positionFocusToken - 1))
                    }
                }

                TokensMeaningsInput.Brackets -> {
                    val id = UUID.randomUUID().toString()
                    listTokenInput.insertToken(
                        TokensMeaningsInput.OpenBrackets.getTokenInput().copy(variableId = id),
                        positionFocusToken
                    )
                    listTokenInput.insertToken(
                        TokensMeaningsInput.CloseBrackets.getTokenInput()
                            .copy(variableId = TokensConst.SeparatorDoubleOperator + id),
                        positionFocusToken + 1
                    )
                    saveTextField(TextRange(if (listTokenInput.size == 2) positionFocusToken else positionFocusToken + 1))
                }

                TokensMeaningsInput.Percentage -> {
                    if (positionFocusToken != 0) {
                        if (listTokenInput[positionFocusToken - 1].tokensMeaningsInput == TokensMeaningsInput.Percentage || listTokenInput[positionFocusToken - 1].tokensMeaningsInput == TokensMeaningsInput.Fractional || listTokenInput[positionFocusToken - 1].tokensMeaningsInput == TokensMeaningsInput.Num || listTokenInput[positionFocusToken - 1].tokensMeaningsInput == TokensMeaningsInput.CloseBrackets) {
                            listTokenInput.insertToken(
                                tokenInput.copy(
                                    variableId = UUID.randomUUID().toString()
                                ), positionFocusToken, removeZero = false
                            )
                            saveTextField(textRangeSelection = TextRange(positionFocusToken + 1))
                            return@with
                        }
                    }
                }

                TokensMeaningsInput.Reset -> {
                    reset()
                }

                else -> {

                }
            }
        }
        return newTextFieldValue
    }

    private fun reset() {
        listTokenInput.clear()
        newTextFieldValue = TextFieldValue(text = "0", selection = TextRange(1))
    }

    private fun saveTextField(textRangeSelection: TextRange /*useAnnotatedString: Boolean = false*/) {
        with(newTextFieldValue) {
            newTextFieldValue = copy(
                text = listTokenInput.map { it.define }.joinToString(separator = ""),
                selection = textRangeSelection
            )
        }
        if (listTokenInput.isEmpty()) {
            reset()
            return
        }
    }

    private fun MutableList<TokenInput>.insertToken(
        tokenInput: TokenInput,
        position: Int,
        removeZero: Boolean = true
    ) {
        if (removeZero) {
            if (size == 1 && first().define == "0") {
                this[0] = tokenInput
                return
            }
        }
        if (positionFocusToken != newTextFieldValue.text.length) {
            add(position, tokenInput)
        } else {
            add(tokenInput)
        }
    }


    private fun preparation(textFieldValue: TextFieldValue) {
        newTextFieldValue = textFieldValue
        if (listTokenInput.isEmpty()) {
            listTokenInput = mutableListOf()
            with(newTextFieldValue) {
                listTokenInput.add(
                    text.getTokenMeanings().getTokenInput(singleDefineToken = text)
                        .copy(variableId = UUID.randomUUID().toString())
                )
            }
        }
        with(newTextFieldValue) {
            positionFocusToken = selection.start
        }
    }

    private fun useGeneralPositionOperator(tokenInput: TokenInput) {
        with(newTextFieldValue) {
            if (positionFocusToken != 0) {

                if (TokensConst.arithmeticOperand.contains(listTokenInput[positionFocusToken - 1].define)) {
                    listTokenInput[positionFocusToken - 1] =
                        tokenInput.copy(variableId = UUID.randomUUID().toString())
                    saveTextField(textRangeSelection = TextRange(positionFocusToken))
                    return@with
                } else {
                    if (positionFocusToken != text.length && TokensConst.arithmeticOperand.contains(
                            listTokenInput[positionFocusToken].define
                        )
                    ) {
                        listTokenInput[positionFocusToken] =
                            tokenInput.copy(variableId = UUID.randomUUID().toString())
                        saveTextField(textRangeSelection = TextRange(positionFocusToken))
                        return@with
                    }
                    listTokenInput.insertToken(
                        tokenInput = tokenInput.copy(
                            variableId = UUID.randomUUID().toString()
                        ), position = positionFocusToken, removeZero = false
                    )
                    saveTextField(textRangeSelection = TextRange(positionFocusToken + 1))
                }
            }
        }
    }

}