package com.example.pruebaqinaya.data


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DefaultResponse(
    @Json(name = "used_trial")
    val usedTrial: String,

    @Json(name = "asigned_computer")
    val asignedComputer: Boolean,
    @Json(name = "session")
    val session: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "name")
    val name: String,

    @Json(name = "user_machine")
    val userMachine: UserMachine
)
