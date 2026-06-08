package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.clubdeportivo.database.DatabaseHelper
import com.example.clubdeportivo.models.Socio

class VistaCarnetActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listCarnets: ListView
    private var listaResultados = mutableListOf<Pair<String, Socio>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_carnet)

        dbHelper = DatabaseHelper(this)
        listCarnets = findViewById(R.id.listCarnets)

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

    private fun cargarLista() {
        listaResultados.clear()
        
        // Cargar Socios
        val socios = dbHelper.obtenerSocios()
        socios.forEach { listaResultados.add(Pair("SOCIO", it)) }

        // Cargar No Socios
        val noSocios = dbHelper.buscarNoSocios("")
        noSocios.forEach { listaResultados.add(Pair("NO_SOCIO", it)) }

        val textos = listaResultados.map { (tipo, s) ->
            val prefijo = if (tipo == "SOCIO") "S" else "NS"
            "${s.nombre} ${s.apellido}\nN° $prefijo-${s.carnetNumero} * DNI: ${s.dni}"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, textos)
        listCarnets.adapter = adapter

        listCarnets.setOnItemClickListener { _, _, position, _ ->
            val seleccion = listaResultados[position]
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
    }
}