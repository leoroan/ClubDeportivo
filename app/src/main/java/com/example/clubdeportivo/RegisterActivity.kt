package com.example.clubdeportivo

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubdeportivo.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var isSocioMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        enableEdgeToEdge()
        setupInsets()
        setupDropdown()
        setupListeners()
        
        val bottomNavigation = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.nav_registro
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Navegando a Inicio", Toast.LENGTH_SHORT).show()
                    startActivity(android.content.Intent(this, MainDashboardActivity::class.java))
                    true
                }
                R.id.nav_registro -> {
                    Toast.makeText(this, "Ya estás en Registro", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_pagos -> {
                    Toast.makeText(this, "Navegando a Pagos", Toast.LENGTH_SHORT).show()
                    startActivity(android.content.Intent(this, PagosActivity::class.java))
                    true
                }
                R.id.nav_carnet -> {
                    Toast.makeText(this, "Navegando a Carnet", Toast.LENGTH_SHORT).show()
                    startActivity(android.content.Intent(this, VistaCarnetActivity::class.java))
                    true
                }
                R.id.nav_perfil -> {
                    Toast.makeText(this, "Navegando a Perfil", Toast.LENGTH_SHORT).show()
                    startActivity(android.content.Intent(this, PerfilUsuarioActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Handle mode from Intent
        val mode = intent.getStringExtra("MODE")
        if (mode == "NO_SOCIO") {
            isSocioMode = false
        }
        
        updateUI()
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupDropdown() {
        val actividades = arrayOf("Gimnasio", "Natación", "Fútbol", "Tenis", "Yoga")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, actividades)
        binding.actividadDropdown.setAdapter(adapter)
    }

    private fun setupListeners() {
        binding.btnTabSocio.setOnClickListener {
            Toast.makeText(this, "Modo Socio seleccionado", Toast.LENGTH_SHORT).show()
            isSocioMode = true
            updateUI()
        }

        binding.btnTabNoSocio.setOnClickListener {
            Toast.makeText(this, "Modo No-Socio seleccionado", Toast.LENGTH_SHORT).show()
            isSocioMode = false
            updateUI()
        }

        binding.btnConfirmar.setOnClickListener {
            val modo = if (isSocioMode) "Socio" else "No-Socio"
            val nombre = binding.etNombre.text.toString()
            
            if (nombre.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese el nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isSocioMode && !binding.cbAptoFisico.isChecked) {
                Toast.makeText(this, "Debe validar el apto físico del socio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Registrando $modo: $nombre", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI() {
        val primaryColor = ContextCompat.getColor(this, R.color.primary)
        val secondaryColor = ContextCompat.getColor(this, R.color.text_secondary)
        val surfaceColor = ContextCompat.getColor(this, R.color.white)
        val activeBg = ContextCompat.getColor(this, R.color.icon_bg_blue)

        if (isSocioMode) {
            // Tab Socio Activa
            binding.btnTabSocio.apply {
                setTextColor(primaryColor)
                setBackgroundColor(activeBg)
                textStyleBold(true)
            }
            binding.btnTabNoSocio.apply {
                setTextColor(secondaryColor)
                setBackgroundColor(surfaceColor)
                textStyleBold(false)
            }

            // Visibilidad de formularios
            binding.layoutSocioExtra.visibility = View.VISIBLE
            binding.layoutNoSocioExtra.visibility = View.GONE
            
            // Textos dinámicos
            binding.btnConfirmar.text = "Registrar Socio"
            binding.tvHeaderTitle.text = "Nuevo Socio"
        } else {
            // Tab Pase Diario Activa
            binding.btnTabNoSocio.apply {
                setTextColor(primaryColor)
                setBackgroundColor(activeBg)
                textStyleBold(true)
            }
            binding.btnTabSocio.apply {
                setTextColor(secondaryColor)
                setBackgroundColor(surfaceColor)
                textStyleBold(false)
            }

            // Visibilidad de formularios
            binding.layoutSocioExtra.visibility = View.GONE
            binding.layoutNoSocioExtra.visibility = View.VISIBLE
            
            // Textos dinámicos
            binding.btnConfirmar.text = "Cobrar Acceso"
            binding.tvHeaderTitle.text = "Pase Diario"
        }
    }

    // Helper para cambiar estilo de texto programáticamente
    private fun View.textStyleBold(isBold: Boolean) {
        if (this is com.google.android.material.button.MaterialButton) {
            this.typeface = if (isBold) 
                android.graphics.Typeface.DEFAULT_BOLD 
            else 
                android.graphics.Typeface.DEFAULT
        }
    }
}