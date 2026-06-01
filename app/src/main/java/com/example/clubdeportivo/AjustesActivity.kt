package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.clubdeportivo.database.DatabaseHelper
import com.example.clubdeportivo.database.SessionManager
import com.example.clubdeportivo.models.Usuario
import com.google.android.material.bottomnavigation.BottomNavigationView

class AjustesActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)

        dbHelper = DatabaseHelper(this)
        sessionManager = SessionManager.getInstance(this)

        // Verificamos si hay sesión, si no, para fines de este ejercicio logueamos al admin por defecto
        if (!sessionManager.isLoggedIn()) {
            val admin = dbHelper.obtenerUsuarioPorUsername("admin")
            if (admin != null) {
                sessionManager.saveSession(admin)
            }
        }

        setupUI()
        loadUserData()
    }

    private fun setupUI() {
        findViewById<CardView>(R.id.btnListadoDeudores)?.setOnClickListener {
            Toast.makeText(this, "Navegando a Listado de Deudores", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, DeudoresActivity::class.java))
        }

        findViewById<CardView>(R.id.btnCrearUsuario)?.setOnClickListener {
            showCrearUsuarioDialog()
        }

        findViewById<CardView>(R.id.btnMiPerfil)?.setOnClickListener {
            startActivity(Intent(this, PerfilUsuarioActivity::class.java))
        }

        findViewById<Button>(R.id.btnCerrarSesion)?.setOnClickListener {
            sessionManager.logout()
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            
            // Cerrar todas las actividades y volver al Login
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.nav_ajuste
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_registro -> {
                    startActivity(Intent(this, RegisterActivity::class.java))
                    true
                }
                R.id.nav_pagos -> {
                    startActivity(Intent(this, PagosActivity::class.java))
                    true
                }
                R.id.nav_carnet -> {
                    startActivity(Intent(this, VistaCarnetActivity::class.java))
                    true
                }
                R.id.nav_ajuste -> {
                    // Ya estamos aquí
                    true
                }
                else -> false
            }
        }
    }

    private fun loadUserData() {
        val user = sessionManager.getUserDetails()
        if (user != null) {
            findViewById<TextView>(R.id.tvAdminNombre).text = user.nombre
            findViewById<TextView>(R.id.tvAdminRol).text = user.rol.replaceFirstChar { it.uppercase() }
        }
    }

    private fun showCrearUsuarioDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_crear_usuario, null)
        val etNombre = dialogView.findViewById<EditText>(R.id.etNombre)
        val etUsername = dialogView.findViewById<EditText>(R.id.etUsername)
        val etPassword = dialogView.findViewById<EditText>(R.id.etPassword)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEmail)
        val etTelefono = dialogView.findViewById<EditText>(R.id.etTelefono)
        val etSede = dialogView.findViewById<EditText>(R.id.etSede)
        val spinnerRol = dialogView.findViewById<Spinner>(R.id.spinnerRol)
        
        val roles = arrayOf("administrador", "empleado")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRol.adapter = adapter

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialogView.findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnGuardar).setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val sede = etSede.text.toString().trim()
            val rol = spinnerRol.selectedItem.toString()

            if (nombre.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || telefono.isEmpty() || sede.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 4) {
                Toast.makeText(this, "La contraseña debe tener al menos 4 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dbHelper.obtenerUsuarioPorUsername(username) != null) {
                Toast.makeText(this, "El nombre de usuario ya existe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevoUsuario = Usuario(
                nombre = nombre,
                username = username,
                password = password,
                rol = rol,
                email = email,
                telefono = telefono,
                sede = sede
            )

            val id = dbHelper.insertarUsuario(nuevoUsuario)
            if (id > 0) {
                Toast.makeText(this, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Error al crear usuario", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}
