package com.example.clubdeportivo

import android.content.Intent
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
import com.example.clubdeportivo.database.DatabaseHelper
import com.example.clubdeportivo.models.Persona
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var isSocioMode = true
    private lateinit var dbHelper: DatabaseHelper
    private var listaActividades: List<com.example.clubdeportivo.models.Actividad> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        enableEdgeToEdge()
        setupInsets()
        setupDropdown()
        setupListeners()

        dbHelper = DatabaseHelper(this)

        val bottomNavigation = binding.bottomNavigation
        bottomNavigation.selectedItemId = R.id.nav_registro
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Navegando a Inicio", Toast.LENGTH_SHORT).show()
                    startActivity(android.content.Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_registro -> {
                    Toast.makeText(this, "Ya estás en Registro", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_pagos -> {
                    Toast.makeText(this, "Navegando a Pagos", Toast.LENGTH_SHORT).show()
                    startActivity(android.content.Intent(this, BuscarPersonaActivity::class.java))
                    true
                }
                R.id.nav_carnet -> {
                    Toast.makeText(this, "Navegando a Carnet", Toast.LENGTH_SHORT).show()
                    startActivity(android.content.Intent(this, VistaCarnetActivity::class.java))
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

        // Handle mode from Intent
        val mode = intent.getStringExtra("MODE")
        if (mode == "NO_SOCIO") {
            isSocioMode = false
        }
        
        updateUI()
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.bottomNavigation.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }
    }

    private fun setupDropdown() {
        dbHelper = DatabaseHelper(this)
        listaActividades = dbHelper.obtenerActividades()
        
        val nombresActividades = listaActividades.map { it.nombre }
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nombresActividades)
        binding.actividadDropdown.setAdapter(adapter)
        
        // Seleccionar la primera por defecto si existe
        if (nombresActividades.isNotEmpty()) {
            binding.actividadDropdown.setText(nombresActividades[0], false)
        }
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

            val nombre = binding.etNombre.text.toString()
            val apellido = binding.etApellido.text.toString()
            val dni = binding.etDni.text.toString()
            val fechaNac = binding.etFechaNacimiento.text.toString()
            val direccion = binding.etDireccion.text.toString()
            val telefono = binding.etTelefono.text.toString()

            if (nombre.isBlank() || dni.isBlank()) {
                Toast.makeText(this, "Nombre y DNI son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {

                val persona = Persona(
                    nombre = nombre,
                    apellido = apellido,
                    fechaNacimiento = fechaNac,
                    direccion = direccion,
                    dni = dni,
                    telefono = telefono,
                    aptoFisico = binding.cbAptoFisico.isChecked
                )

                if (isSocioMode) {

                    val resultado =
                        dbHelper.registrarSocio(
                            persona = persona,
                            fechaInscripcion = "2026-06-08",
                            fechaVencimiento = "2026-07-08",
                            importeMensual = 15000.0
                        )

                    if (resultado > 0) {

                        Toast.makeText(
                            this,
                            "Socio registrado correctamente",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {

                        Toast.makeText(
                            this,
                            "Error al registrar socio",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } else {

                    val nombreActividadSeleccionada = binding.actividadDropdown.text.toString()
                    val actividad = listaActividades.find { it.nombre == nombreActividadSeleccionada }

                    if (actividad == null) {
                        Toast.makeText(this, "Por favor seleccione una actividad válida", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }

                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val fechaHoy = sdf.format(Date())

                    val resultado =
                        dbHelper.registrarNoSocio(
                            persona = persona,
                            actividad = actividad,
                            fecha = fechaHoy
                        )


                    if (resultado > 0) {

                        Toast.makeText(
                            this,
                            "No socio registrado",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {

                        Toast.makeText(
                            this,
                            "Error al registrar",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(
                    this,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
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

    private fun View.textStyleBold(isBold: Boolean) {
        if (this is com.google.android.material.button.MaterialButton) {
            this.typeface = if (isBold) 
                android.graphics.Typeface.DEFAULT_BOLD 
            else 
                android.graphics.Typeface.DEFAULT
        }
    }
}