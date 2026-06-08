package com.example.clubdeportivo.models

data class Persona(
    val idPersona: Long = 0,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: String,
    val direccion: String,
    val dni: String,
    val telefono: String,
    val aptoFisico: Boolean
)