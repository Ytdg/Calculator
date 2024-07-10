package com.example.calculator.domain.usecase

import javax.inject.Inject

class UseCases @Inject constructor(
    val useCaseInputExpression: UseCaseInputExpression,
    val useCaseCalculate: UseCaseCalculate
) {
}