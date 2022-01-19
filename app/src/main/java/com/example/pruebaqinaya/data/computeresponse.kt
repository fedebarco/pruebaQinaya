package com.example.pruebaqinaya

import com.example.pruebaqinaya.data.UserMachine
import com.google.gson.annotations.SerializedName


data class computeresponse(
    @SerializedName("user_machine")
    val userMachine: UserMachine,
)


