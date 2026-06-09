package com.example.clubdeportivo.models

/**
 * Representa a un cliente con deuda pendiente.
 */
data class Deudor(
    val nombre: String,
    val dni: String,
    val montoAdeudado: Double
)
