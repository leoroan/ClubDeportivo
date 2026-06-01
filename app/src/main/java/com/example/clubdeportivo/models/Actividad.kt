package com.example.clubdeportivo.models

/**
 * Representa una actividad ofrecida por el club.
 */
data class Actividad(
    val nombre: String,
    val precioSocio: Double? = null,
    val precioNoSocio: Double? = null
)
