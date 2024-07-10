package com.example

internal fun String.toToken(): Token{
    toDoubleOrNull()?.let {
        return TokensMeanings.Num.getToken().copy(define = this, tokensMeanings = TokensMeanings.Num)
    }
    return  this.getOperandTokenMeanings().getToken().copy(define = this)

}