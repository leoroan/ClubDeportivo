package com.example.clubdeportivo.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.clubdeportivo.models.Usuario

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ClubDeportivo.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_USUARIOS = "usuarios"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_ROL = "rol"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_TELEFONO = "telefono"
        const val COLUMN_SEDE = "sede"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE " + TABLE_USUARIOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOMBRE + " TEXT NOT NULL,"
                + COLUMN_USERNAME + " TEXT NOT NULL UNIQUE,"
                + COLUMN_PASSWORD + " TEXT NOT NULL,"
                + COLUMN_ROL + " TEXT NOT NULL,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_TELEFONO + " TEXT,"
                + COLUMN_SEDE + " TEXT" + ")")
        db.execSQL(createTable)

        // Insert default admin
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, "Administrador")
            put(COLUMN_USERNAME, "admin")
            put(COLUMN_PASSWORD, "admin123")
            put(COLUMN_ROL, "administrador-general")
            put(COLUMN_EMAIL, "admin@clubdeportivo.com")
            put(COLUMN_TELEFONO, "+54 11 1234-5678")
            put(COLUMN_SEDE, "Sede Central")
        }
        db.insert(TABLE_USUARIOS, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        onCreate(db)
    }

    fun insertarUsuario(usuario: Usuario): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, usuario.nombre)
            put(COLUMN_USERNAME, usuario.username)
            put(COLUMN_PASSWORD, usuario.password)
            put(COLUMN_ROL, usuario.rol)
            put(COLUMN_EMAIL, usuario.email)
            put(COLUMN_TELEFONO, usuario.telefono)
            put(COLUMN_SEDE, usuario.sede)
        }
        return db.insert(TABLE_USUARIOS, null, values)
    }

    fun obtenerUsuarioPorUsername(username: String): Usuario? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS, null, "$COLUMN_USERNAME=?", arrayOf(username),
            null, null, null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val usuario = cursorToUsuario(cursor)
            cursor.close()
            usuario
        } else {
            null
        }
    }

    fun obtenerTodosLosUsuarios(): List<Usuario> {
        val usuariosList = mutableListOf<Usuario>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USUARIOS", null)
        if (cursor.moveToFirst()) {
            do {
                usuariosList.add(cursorToUsuario(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return usuariosList
    }

    fun actualizarUsuario(usuario: Usuario): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, usuario.nombre)
            put(COLUMN_USERNAME, usuario.username)
            put(COLUMN_PASSWORD, usuario.password)
            put(COLUMN_ROL, usuario.rol)
            put(COLUMN_EMAIL, usuario.email)
            put(COLUMN_TELEFONO, usuario.telefono)
            put(COLUMN_SEDE, usuario.sede)
        }
        return db.update(TABLE_USUARIOS, values, "$COLUMN_ID=?", arrayOf(usuario.id.toString()))
    }

    fun eliminarUsuario(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_USUARIOS, "$COLUMN_ID=?", arrayOf(id.toString()))
    }

    fun autenticar(username: String, password: String): Usuario? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS, null, "$COLUMN_USERNAME=? AND $COLUMN_PASSWORD=?",
            arrayOf(username, password), null, null, null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val usuario = cursorToUsuario(cursor)
            cursor.close()
            usuario
        } else {
            null
        }
    }

    private fun cursorToUsuario(cursor: Cursor): Usuario {
        return Usuario(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
            nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
            username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
            password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
            rol = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROL)),
            email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
            telefono = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO)),
            sede = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SEDE))
        )
    }
}
