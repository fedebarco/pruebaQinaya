package com.example.pruebaqinaya.data

import com.squareup.moshi.Json

data class CountriesResponse(
    @Json(name = "id")var id:String,
    @Json (name = "name")var name:String

)
