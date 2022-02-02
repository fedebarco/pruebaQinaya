package com.example.pruebaqinaya.data

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class PruebaResponse(
    @Json( name = "used_trial")
    val usedTrial: String,

    @Json(name = "asigned_computer")
    val asignedComputer: Boolean,

    val session: String,

    @Json(name = "user_machine")
    val userMachine: UserMachine
)
