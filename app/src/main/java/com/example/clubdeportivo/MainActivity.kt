package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clubdeportivo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCardListeners()
        setupBottomNavigation()
    }

    private fun setupCardListeners() {
        binding.cardRegistrar.setOnClickListener {
            Toast.makeText(this, "Abriendo registro de nuevo socio", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.cardCobrarPago.setOnClickListener {
            Toast.makeText(this, "Abriendo sección de pagos", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PagosActivity::class.java)
            startActivity(intent)
        }

        binding.cardAjusteMas.setOnClickListener {
            Toast.makeText(this, "Abriendo registro de no-socio (Pase Diario)", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AjustesActivity::class.java)
            intent.putExtra("MODE", "NO_SOCIO")
            startActivity(intent)
        }

        binding.cardGenerarCarnet.setOnClickListener {
            Toast.makeText(this, "Abriendo visor de carnets", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, VistaCarnetActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Ya estás en Inicio", Toast.LENGTH_SHORT).show()
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
