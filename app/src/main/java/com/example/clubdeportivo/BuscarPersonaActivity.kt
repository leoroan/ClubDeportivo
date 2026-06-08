package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.clubdeportivo.models.Socio
import com.example.clubdeportivo.database.DatabaseHelper
import com.google.android.material.bottomnavigation.BottomNavigationView


class BuscarPersonaActivity : AppCompatActivity() {

    private lateinit var etBuscar: EditText
    private lateinit var listSocios: ListView

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: ArrayAdapter<String>
    private var listaResultados = mutableListOf<Pair<String, Socio>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_persona)

        dbHelper = DatabaseHelper(this)

        etBuscar = findViewById(R.id.etBuscar)
        listSocios = findViewById(R.id.listSocios)

        actualizarLista("")

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                actualizarLista(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        listSocios.setOnItemClickListener { _, _, position, _ ->
            val seleccion = listaResultados[position]
            val intent = Intent(this, PagosActivity::class.java)
            intent.putExtra("socio", seleccion.second)
            intent.putExtra("tipo", seleccion.first) // "SOCIO" o "NO_SOCIO"
            startActivity(intent)
        }

        setupUI()
    }
    private fun setupUI() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.nav_ajuste
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
    private fun actualizarLista(filtro: String) {
        listaResultados.clear()
        
        // Buscar Socios
        val socios = dbHelper.buscarSocios(filtro)
        socios.forEach { listaResultados.add(Pair("SOCIO", it)) }

        // Buscar No Socios
        val noSocios = dbHelper.buscarNoSocios(filtro)
        noSocios.forEach { listaResultados.add(Pair("NO_SOCIO", it)) }

        val textos = listaResultados.map { (tipo, s) ->
            val prefijo = if (tipo == "SOCIO") "S" else "NS"
            "$prefijo-${s.carnetNumero} - ${s.nombre} ${s.apellido} - DNI ${s.dni}"
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, textos)
        listSocios.adapter = adapter
    }
}
