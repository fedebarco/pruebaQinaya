package com.example.pruebaqinaya.data

import com.squareup.moshi.Json

data class TrialResponse(
    @Json( name="Status")
    val status: Boolean,

    @Json(name = "Message")
    val message: String,
)
