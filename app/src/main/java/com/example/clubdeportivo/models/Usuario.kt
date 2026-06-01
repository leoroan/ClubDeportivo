package com.example.clubdeportivo.models

/**
 * Representa al personal o administrador del club.
 */
data class Usuario(
    val nombre: String,
    val rol: String,
    val email: String,
    val telefono: String,
    val sede: String
)
