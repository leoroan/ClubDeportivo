package com.example.clubdeportivo.models

data class Cuota(
    val idCuota: Long,
    val carnetNumero: Long,
    val fechaGeneracion: String,
    val vencimiento: String,
    val importe: Double,
    val estado: Int
)