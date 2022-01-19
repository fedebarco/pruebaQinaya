package com.example.pruebaqinaya.data


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserMachine(
    @Json(name="id")
    val id: Long,
    @Json(name="plan_activo")
    val planActivo: Boolean,
    @Json(name="tiempo_disponible")
    val tiempoDisponible: String,
    @Json(name="start_date")
    val startDate: String,
    @Json(name="end_date")
    val endDate: String,
    @Json(name="nombre_maquina")
    val nombreMaquina: String,
    @Json(name="default_machine")
    val defaultMachine: Boolean,
    @Json(name="sistema_operativo")
    val sistemaOperativo: String,
    @Json(name="logo_so")
    val logoSo: String,
    val plan: String,
    val url: String,
    @Json(name="server_status")
    val serverStatus: Any?=null,
)
