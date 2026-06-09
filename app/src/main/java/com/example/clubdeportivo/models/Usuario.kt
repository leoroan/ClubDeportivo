package com.example.clubdeportivo.models

/**
 * Representa al personal o administrador del club.
 */
data class Usuario(
    val id: Int? = null,
    val nombre: String,
    val username: String,
    val password: String,
    val rol: String,
    val email: String,
    val telefono: String,
    val sede: String
)
