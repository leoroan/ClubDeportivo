package com.example.clubdeportivo.models
import java.io.Serializable

/**
 * Representa a un socio regular del club.
 */
data class Socio(
    val carnetNumero: Long,
    val idPersona: Long,
    val nombre: String,
    val apellido: String,
    val dni: String,
    val telefono: String,
    val direccion: String,
    val fechaNacimiento: String,
    val aptoFisico: Boolean,
    val fechaInscripcion: String,
    val activo: Boolean,
    val vencimiento: String = ""
) : java.io.Serializable