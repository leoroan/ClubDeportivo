package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class VistaCarnetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_carnet)

        val btnCarnetSocio = findViewById<LinearLayout>(R.id.btncarnet)
        btnCarnetSocio.setOnClickListener {
            Toast.makeText(this, "Mostrando carnet de Socio", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, CarnetSocioActivity::class.java))
        }

        val btnCarnetNoSocio = findViewById<LinearLayout>(R.id.btncarnet_no_socio)
        btnCarnetNoSocio.setOnClickListener {
            Toast.makeText(this, "Mostrando carnet de No-Socio", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, CarnetNoSocioActivity::class.java))
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.nav_carnet
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
                    Toast.makeText(this, "Ya estás en Carnet", Toast.LENGTH_SHORT).show()
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