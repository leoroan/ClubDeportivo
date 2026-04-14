package com.example.clubdeportivo.models

import java.io.Serializable

data class Usuario(
    val id: Int,
    val nombre: String,
    val username: String,
    val password: String,
    val rol: String
) : Serializable {}