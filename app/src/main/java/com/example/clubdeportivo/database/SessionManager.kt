package com.example.clubdeportivo.database

import android.content.Context
import android.content.SharedPreferences
import com.example.clubdeportivo.models.Usuario

class SessionManager private constructor(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "ClubDeportivoSession"
        private const val KEY_ID = "user_id"
        private const val KEY_NOMBRE = "user_nombre"
        private const val KEY_USERNAME = "user_username"
        private const val KEY_ROL = "user_rol"
        private const val KEY_EMAIL = "user_email"
        private const val KEY_TELEFONO = "user_telefono"
        private const val KEY_SEDE = "user_sede"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"

        @Volatile
        private var INSTANCE: SessionManager? = null

        fun getInstance(context: Context): SessionManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SessionManager(context).also { INSTANCE = it }
            }
        }
    }

    fun saveSession(usuario: Usuario) {
        val editor = prefs.edit()
        editor.putInt(KEY_ID, usuario.id ?: -1)
        editor.putString(KEY_NOMBRE, usuario.nombre)
        editor.putString(KEY_USERNAME, usuario.username)
        editor.putString(KEY_ROL, usuario.rol)
        editor.putString(KEY_EMAIL, usuario.email)
        editor.putString(KEY_TELEFONO, usuario.telefono)
        editor.putString(KEY_SEDE, usuario.sede)
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.apply()
    }

    fun getUserDetails(): Usuario? {
        if (!prefs.getBoolean(KEY_IS_LOGGED_IN, false)) return null

        return Usuario(
            id = prefs.getInt(KEY_ID, -1),
            nombre = prefs.getString(KEY_NOMBRE, "") ?: "",
            username = prefs.getString(KEY_USERNAME, "") ?: "",
            password = "", // No guardamos la contraseña en sesión por seguridad
            rol = prefs.getString(KEY_ROL, "") ?: "",
            email = prefs.getString(KEY_EMAIL, "") ?: "",
            telefono = prefs.getString(KEY_TELEFONO, "") ?: "",
            sede = prefs.getString(KEY_SEDE, "") ?: ""
        )
    }

    fun logout() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
}
