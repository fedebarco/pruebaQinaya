package com.example.pruebaqinaya

import com.google.gson.annotations.SerializedName
data class Root (
    @SerializedName("")
    val lista:List<Root2>,
)

data class Root2(
    @SerializedName("user_machine")
    val userMachine: UserMachine,
)

data class UserMachine(
    val id: Long,
    @SerializedName("plan_activo")
    val planActivo: Boolean,
    @SerializedName("tiempo_disponible")
    val tiempoDisponible: String,
    @SerializedName("used_trial")
    val usedTrial: String,
    @SerializedName("nombre_maquina")
    val nombreMaquina: String,
    @SerializedName("default_machine")
    val defaultMachine: Boolean,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("sistema_operativo")
    val sistemaOperativo: String,
    @SerializedName("logo_so")
    val logoSo: String,
    val plan: String,
    val url: String,
    @SerializedName("server_status")
    val serverStatus: Any?,
)

