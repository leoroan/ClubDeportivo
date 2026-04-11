package com.example.clubdeportivo.database.usuario

import android.content.ContentValues
import com.example.clubdeportivo.database.db.DbHelper

class UsuarioDao(private val dbHelper: DbHelper) {

    fun insertar(nombre: String, username: String, password: String): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("nombre", nombre)
            put("username", username)
            put("password", password)
        }

        return db.insert("usuarios", null, values)
    }

    fun validar(username: String, password: String): Boolean {
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            "SELECT id FROM usuarios WHERE username = ? AND password = ?",
            arrayOf(username, password)
        )

        val existe = cursor.count > 0
        cursor.close()

        return existe
    }
}