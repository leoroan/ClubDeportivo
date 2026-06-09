package com.example.clubdeportivo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clubdeportivo.database.DatabaseHelper
import com.example.clubdeportivo.models.Socio
import com.example.clubdeportivo.models.Cuota
import com.google.android.material.bottomnavigation.BottomNavigationView



class PagosActivity : AppCompatActivity() {
    private lateinit var txtNombre: TextView
    private lateinit var txtDetalle: TextView
    private lateinit var txtVencimiento: TextView
    private lateinit var txtMonto: TextView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos)

        // Referencias UI
        txtNombre = findViewById(R.id.txtNombre)
        txtDetalle = findViewById(R.id.txtDetalle)
        txtVencimiento = findViewById(R.id.txtVencimiento)
        txtMonto = findViewById(R.id.txtMonto)

        // Inicializar DB
        dbHelper = DatabaseHelper(this)

        findViewById<android.widget.ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val tipo = intent.getStringExtra("tipo") ?: intent.getStringExtra("TIPO")

        val socio = if (Build.VERSION.SDK_INT >= 33) {
            intent.getSerializableExtra("socio", Socio::class.java) 
                ?: intent.getSerializableExtra("SOCIO", Socio::class.java)
        } else {
            @Suppress("DEPRECATION")
            (intent.getSerializableExtra("socio") as? Socio) 
                ?: (intent.getSerializableExtra("SOCIO") as? Socio)
        }



        if (socio != null) {
            txtNombre.text = "${socio.nombre} ${socio.apellido}"
            txtDetalle.text = if (tipo == "SOCIO") {
                "Socio Carnet: ${socio.carnetNumero}\nDNI: ${socio.dni}"
            } else {
                "No Socio Temporal: ${socio.carnetNumero}\nDNI: ${socio.dni}"
            }

            if (tipo == "SOCIO") {
                val cuota = dbHelper.obtenerCuotaPendiente(socio.carnetNumero)
                if (cuota != null) {
                    txtVencimiento.text = cuota.vencimiento
                    txtMonto.text = "$${cuota.importe}"
                } else {
                    txtVencimiento.text = "Sin deuda"
                    txtMonto.text = "$0"
                }
            } else {
                // Lógica para No Socio
                val pagoAct = dbHelper.obtenerPagoActividadPendiente(socio.carnetNumero)
                if (pagoAct != null) {
                    txtVencimiento.text = "Pago Actividad"
                    txtMonto.text = "$${pagoAct.second}"
                } else {
                    txtVencimiento.text = "Sin pagos pendientes"
                    txtMonto.text = "$0"
                }
            }
        } else {
            txtNombre.text = "Persona no seleccionada"
            txtDetalle.text = "Seleccione una persona desde la búsqueda"
            txtVencimiento.text = "-"
            txtMonto.text = "$0"
        }

        val btnConfirmar = findViewById<Button>(R.id.btnConfirmar)
        btnConfirmar.setOnClickListener {
            if (socio == null) {
                Toast.makeText(this, "No hay persona seleccionada", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (tipo == "SOCIO") {
                val cuota = dbHelper.obtenerCuotaPendiente(socio.carnetNumero)
                if (cuota != null) {
                    dbHelper.registrarPagoCuota(cuota.idCuota, cuota.importe, "Efectivo")
                    Toast.makeText(this, "Cuota pagada correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "El socio no posee deuda", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Pago para No Socio
                val pagoAct = dbHelper.obtenerPagoActividadPendiente(socio.carnetNumero)
                if (pagoAct != null) {
                    dbHelper.marcarPagoActividadPagado(pagoAct.first)
                    Toast.makeText(this, "Actividad pagada correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "No hay actividad pendiente de pago", Toast.LENGTH_SHORT).show()
                }
            }
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