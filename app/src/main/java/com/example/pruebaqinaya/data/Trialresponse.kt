package com.example.pruebaqinaya.data

import com.google.gson.annotations.SerializedName

data class trialresponse(
    @SerializedName( "Status")
    val status: Boolean,

    @SerializedName("Message")
    val message: String,
)
