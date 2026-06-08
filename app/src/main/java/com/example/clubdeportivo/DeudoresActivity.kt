package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clubdeportivo.adapters.DeudorAdapter
import com.example.clubdeportivo.database.DatabaseHelper
import com.example.clubdeportivo.databinding.ActivityDeudoresBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class DeudoresActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeudoresBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeudoresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        setupRecyclerView()

        binding.btnBack.setOnClickListener {
            finish()
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.nav_ajuste // O el que corresponda
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
                    // Ya estamos en una subsección de ajustes, pero podríamos volver a AjustesActivity
                    startActivity(Intent(this, AjustesActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        val deudores = dbHelper.obtenerDeudores()
        
        if (deudores.isEmpty()) {
            Toast.makeText(this, "No hay deudores registrados", Toast.LENGTH_SHORT).show()
        }

        val adapter = DeudorAdapter(deudores)
        binding.rvDeudores.layoutManager = LinearLayoutManager(this)
        binding.rvDeudores.adapter = adapter
    }
}