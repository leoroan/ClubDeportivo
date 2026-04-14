package com.example.clubdeportivo.database.usuario

import android.content.ContentValues
import com.example.clubdeportivo.database.db.DbHelper
import com.example.clubdeportivo.models.Usuario

class UsuarioDao(private val dbHelper: DbHelper) {

    fun insertar(nombre: String, username: String, password: String, rol: String): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("nombre", nombre)
            put("username", username)
            put("password", password)
            put("rol", rol)
        }

        return db.insert("usuarios", null, values)
    }

    fun validar(username: String, password: String): Usuario? {
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE username=? AND password=?",
            arrayOf(username, password)
        )

        return if (cursor.moveToFirst()) {
            val usuario = Usuario(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                rol = cursor.getString(cursor.getColumnIndexOrThrow("rol"))
            )
            cursor.close()
            usuario
        } else {
            cursor.close()
            null
        }
    }
}