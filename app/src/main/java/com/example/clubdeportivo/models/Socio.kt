package com.example.clubdeportivo.models

/**
 * Representa a un socio regular del club.
 */
data class Socio(
    val nombre: String,
    val dni: String,
    val nroSocio: String,
    val aptoFisico: Boolean = false,
    val vencimiento: String
)
