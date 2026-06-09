package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.clubdeportivo.adapters.PersonaAdapter
import com.example.clubdeportivo.database.DatabaseHelper
import com.example.clubdeportivo.models.Socio
import com.google.android.material.bottomnavigation.BottomNavigationView


class VistaCarnetActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var rvCarnets: RecyclerView
    private lateinit var adapter: PersonaAdapter
    private var listaResultados = mutableListOf<Pair<String, Socio>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_carnet)

        dbHelper = DatabaseHelper(this)
        rvCarnets = findViewById(R.id.rvCarnets)

        findViewById<android.widget.ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        setupRecyclerView()
        cargarLista()

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.nav_carnet
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
                    startActivity(Intent(this, BuscarPersonaActivity::class.java))
                    true
                }
                R.id.nav_carnet -> {
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

    private fun setupRecyclerView() {
        adapter = PersonaAdapter(listaResultados) { seleccion ->
            val tipo = seleccion.first
            val socio = seleccion.second

            if (tipo == "SOCIO") {
                val intent = Intent(this, CarnetSocioActivity::class.java)
                intent.putExtra("socio", socio)
                startActivity(intent)
            } else {
                val intent = Intent(this, CarnetNoSocioActivity::class.java)
                intent.putExtra("socio", socio)
                startActivity(intent)
            }
        }
        rvCarnets.adapter = adapter
    }

    private fun cargarLista() {
        listaResultados.clear()
        
        // Cargar Socios
        val socios = dbHelper.obtenerSocios()
        socios.forEach { listaResultados.add(Pair("SOCIO", it)) }

        // Cargar No Socios
        val noSocios = dbHelper.buscarNoSocios("")
        noSocios.forEach { listaResultados.add(Pair("NO_SOCIO", it)) }

        adapter.updateData(listaResultados)
    }
}