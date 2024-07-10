package com.example

import java.util.UUID

internal data class  Token(
    val define:String,
    val tokensMeanings:TokensMeanings,
    val priority:Int,
    val id:UUID=UUID.randomUUID()
)

