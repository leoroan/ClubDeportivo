package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class PagosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos)

        val btnConfirmar = findViewById<Button>(R.id.btnConfirmar)
        btnConfirmar.setOnClickListener {
            Toast.makeText(this, "Pago confirmado exitosamente", Toast.LENGTH_SHORT).show()
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.nav_pagos
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Navegando a Inicio", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_registro -> {
                    Toast.makeText(this, "Navegando a Registro", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, RegisterActivity::class.java))
                    true
                }
                R.id.nav_pagos -> {
                    Toast.makeText(this, "Ya estás en Pagos", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_carnet -> {
                    Toast.makeText(this, "Navegando a Carnet", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, VistaCarnetActivity::class.java))
                    true
                }
                R.id.nav_ajuste -> {
                    Toast.makeText(this, "Navegando a ajuste y mas", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, AjustesActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}