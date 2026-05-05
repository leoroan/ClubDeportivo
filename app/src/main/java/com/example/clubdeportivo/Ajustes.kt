package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Ajustes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)

        findViewById<CardView>(R.id.btnListadoDeudores)?.setOnClickListener {
            Toast.makeText(this, "Navegando a Listado de Deudores", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Deudores::class.java))
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Navegando a Inicio", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainDashboardActivity::class.java))
                    true
                }
                R.id.nav_registro -> {
                    Toast.makeText(this, "Navegando a Registro", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, RegisterActivity::class.java))
                    true
                }
                R.id.nav_pagos -> {
                    Toast.makeText(this, "Navegando a Pagos", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, PagosActivity::class.java))
                    true
                }
                R.id.nav_carnet -> {
                    Toast.makeText(this, "Navegando a Carnet", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, VistaCarnetActivity::class.java))
                    true
                }
                R.id.nav_perfil -> {
                    Toast.makeText(this, "Navegando a Perfil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, PerfilUsuarioActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}