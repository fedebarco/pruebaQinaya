package com.example.pruebaqinaya.data

import com.google.gson.annotations.SerializedName

data class pruebaRsponse(
    @SerializedName( "used_trial")
    val usedTrial: String,

    @SerializedName("asigned_computer")
    val asignedComputer: Boolean,

    val session: String,

    @SerializedName("user_machine")
    val userMachine: UserMachine
)
