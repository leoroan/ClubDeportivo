package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clubdeportivo.database.SessionManager
import com.example.clubdeportivo.databinding.ActivityPerfilUsuarioBinding

class PerfilUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilUsuarioBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager.getInstance(this)
        
        binding.btnBack.setOnClickListener {
            finish()
        }

        setupUI()
        loadUserData()
    }
    
    private fun setupUI() {
        binding.btnVerDeudores.setOnClickListener {
            Toast.makeText(this, "Navegando a Deudores", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, DeudoresActivity::class.java))
        }

        binding.bottomNavigation.selectedItemId = R.id.nav_ajuste
        binding.bottomNavigation.setOnItemSelectedListener { item ->
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
        user?.let {
            binding.tvPerfilNombre.text = it.nombre
            binding.tvPerfilRol.text = it.rol.replaceFirstChar { char -> char.uppercase() }
            binding.tvPerfilEmail.text = it.email
            binding.tvPerfilTelefono.text = it.telefono
            binding.tvPerfilSede.text = it.sede
        }
    }
}
