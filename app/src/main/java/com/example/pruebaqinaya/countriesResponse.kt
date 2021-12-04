package com.example.pruebaqinaya

import com.google.gson.annotations.SerializedName

data class countriesResponse(
    @SerializedName ("id")var id:String,
    @SerializedName ("name")var name:String

)
