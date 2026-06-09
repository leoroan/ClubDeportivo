package com.example.clubdeportivo.models

/**
 * Representa un registro de pago realizado o por realizar.
 */
data class Pago(
    val nombreCliente: String,
    val dniCliente: String,
    val nroSocio: String? = null,
    val monto: Double,
    val vencimiento: String,
    var metodoPago: String? = null
)
