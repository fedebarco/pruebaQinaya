package com.example.pruebaqinaya.data

import com.squareup.moshi.Json


data class Registration(
    @Json(name = "name")
    val name: String,

    @Json(name = "user")
    val user: String,

    @Json(name = "country")
    val country: Int,

    @Json(name = "phone")
    val phone: String,

    @Json(name = "subscription")
    val sub: Int,

    @Json(name = "currency")
    val currency: Int,

    @Json(name = "password")
    val password: String,


)
