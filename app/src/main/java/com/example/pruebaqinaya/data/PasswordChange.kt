package com.example.pruebaqinaya.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PasswordChange(
    @Json(name = "id")
    val id: Long,

    @Json(name = "password")
    val passwordOld: String,

    @Json(name = "new_password")
    val passwordnew: String


)
