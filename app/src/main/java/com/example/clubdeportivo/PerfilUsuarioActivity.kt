package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clubdeportivo.database.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class PerfilUsuarioActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_usuario)

        sessionManager = SessionManager.getInstance(this)

        setupUI()
        loadUserData()
    }

    private fun setupUI() {
        val btnVerDeudores = findViewById<Button>(R.id.btn_ver_deudores)
        btnVerDeudores.setOnClickListener {
            Toast.makeText(this, "Navegando a Deudores", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, DeudoresActivity::class.java))
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
                    startActivity(Intent(this, AjustesActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun loadUserData() {
        val user = sessionManager.getUserDetails()
        if (user != null) {
            findViewById<TextView>(R.id.tvPerfilNombre).text = user.nombre
            findViewById<TextView>(R.id.tvPerfilRol).text = user.rol.replaceFirstChar { it.uppercase() }
            findViewById<TextView>(R.id.tvPerfilEmail).text = user.email
            findViewById<TextView>(R.id.tvPerfilTelefono).text = user.telefono
            findViewById<TextView>(R.id.tvPerfilSede).text = user.sede
        }
    }
}
