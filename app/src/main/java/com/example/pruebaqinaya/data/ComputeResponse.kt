package com.example.pruebaqinaya

import com.example.pruebaqinaya.data.UserMachine
import com.squareup.moshi.Json


data class ComputeResponse(
    @Json(name = "user_machine")
    val userMachine: UserMachine,
)


