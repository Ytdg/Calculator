package com.example.calculator.domain.usecase

import com.example.calculation.Calculater
import com.example.calculator.domain.ResultCalculate
import javax.inject.Inject

class UseCaseCalculate @Inject constructor(){
    operator fun invoke(expression: String): ResultCalculate<String> {
        try {
            val calc = Calculater(expression.toList().map { it.toString() })
            return ResultCalculate.Successfully(value = calc.CalculateExpression())
        } catch (ex: Exception) {
            return  ResultCalculate.Error("Ошибка")
        }
    }
}
