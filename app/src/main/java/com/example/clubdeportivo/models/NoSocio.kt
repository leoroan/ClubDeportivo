package com.example.clubdeportivo.models

/**
 * Representa a un cliente temporal (Pase Diario).
 */
data class NoSocio(
    val nombre: String,
    val dni: String,
    val actividad: String,
    val vencimiento: String
)
