package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.clubdeportivo.adapters.PersonaAdapter
import com.example.clubdeportivo.models.Socio
import com.example.clubdeportivo.database.DatabaseHelper
import com.google.android.material.bottomnavigation.BottomNavigationView


class BuscarPersonaActivity : AppCompatActivity() {

    private lateinit var etBuscar: EditText
    private lateinit var rvSocios: RecyclerView

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: PersonaAdapter
    private var listaResultados = mutableListOf<Pair<String, Socio>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_persona)

        dbHelper = DatabaseHelper(this)

        etBuscar = findViewById(R.id.etBuscar)
        rvSocios = findViewById(R.id.rvSocios)

        findViewById<android.widget.ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        setupRecyclerView()

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                actualizarLista(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        setupUI()
    }

    override fun onResume() {
        super.onResume()
        // Refrescar la lista cada vez que volvemos a la actividad
        actualizarLista(etBuscar.text.toString())
    }

    private fun setupRecyclerView() {
        adapter = PersonaAdapter(listaResultados) { seleccion ->
            val intent = Intent(this, PagosActivity::class.java)
            intent.putExtra("socio", seleccion.second)
            intent.putExtra("tipo", seleccion.first) // "SOCIO" o "NO_SOCIO"
            startActivity(intent)
        }
        rvSocios.adapter = adapter
    }

    private fun setupUI() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.nav_pagos
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
    private fun actualizarLista(filtro: String) {
        listaResultados.clear()
        
        // Buscar Socios con deuda
        val socios = dbHelper.buscarSociosDeudores(filtro)
        socios.forEach { listaResultados.add(Pair("SOCIO", it)) }

        // Buscar No Socios con deuda (actividades pendientes)
        val noSocios = dbHelper.buscarNoSociosDeudores(filtro)
        noSocios.forEach { listaResultados.add(Pair("NO_SOCIO", it)) }

        adapter.updateData(listaResultados)
    }
}
