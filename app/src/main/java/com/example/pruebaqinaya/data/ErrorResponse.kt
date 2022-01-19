package com.example.pruebaqinaya.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "Status")
    val status: Boolean,

    @Json(name = "Message")
    val message: String
)
