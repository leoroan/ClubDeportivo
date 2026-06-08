package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

import android.widget.TextView
import com.example.clubdeportivo.models.Socio

class CarnetSocioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnet_socio)

        val socio = intent.getSerializableExtra("socio") as? Socio

        socio?.let {
            findViewById<TextView>(R.id.tvNombreSocio).text = "${it.nombre} ${it.apellido}"
            findViewById<TextView>(R.id.tvDetallesSocio).text = "N° S-${it.carnetNumero} * DNI: ${it.dni}"
            findViewById<TextView>(R.id.tvVencimientoSocio).text = "VENCE ${it.vencimiento.ifEmpty { "N/A" }}"
        }

        findViewById<Button>(R.id.btn_cerrar)?.setOnClickListener {
            Toast.makeText(this, "Cerrando carnet", Toast.LENGTH_SHORT).show()
            finish()
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.nav_carnet
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
                    Toast.makeText(this, "Navegando a Pagos", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, PagosActivity::class.java))
                    true
                }
                R.id.nav_carnet -> {
                    Toast.makeText(this, "Ya estás en Carnet", Toast.LENGTH_SHORT).show()
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