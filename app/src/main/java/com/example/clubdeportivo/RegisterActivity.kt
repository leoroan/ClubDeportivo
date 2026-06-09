package com.example.clubdeportivo

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubdeportivo.databinding.ActivityRegisterBinding
import com.example.clubdeportivo.database.DatabaseHelper
import com.example.clubdeportivo.models.Persona
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var isSocioMode = true
    private lateinit var dbHelper: DatabaseHelper
    private var listaActividades: List<com.example.clubdeportivo.models.Actividad> = emptyList()
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        enableEdgeToEdge()
        setupInsets()
        setupDropdown()
        setupListeners()
        setupBackPressed()

        dbHelper = DatabaseHelper(this)

        val bottomNavigation = binding.bottomNavigation
        bottomNavigation.selectedItemId = R.id.nav_registro
        bottomNavigation.setOnItemSelectedListener { item ->
            if (hasUnsavedChanges()) {
                showExitConfirmationDialog {
                    navigateTo(item.itemId)
                }
                false
            } else {
                navigateTo(item.itemId)
                true
            }
        }

        // Handle mode from Intent
        val mode = intent.getStringExtra("MODE")
        if (mode == "NO_SOCIO") {
            isSocioMode = false
        }
        
        updateUI()
    }

    private fun navigateTo(itemId: Int) {
        when (itemId) {
            R.id.nav_home -> startActivity(Intent(this, MainActivity::class.java))
            R.id.nav_registro -> Toast.makeText(this, "Ya estás en Registro", Toast.LENGTH_SHORT).show()
            R.id.nav_pagos -> startActivity(Intent(this, BuscarPersonaActivity::class.java))
            R.id.nav_carnet -> startActivity(Intent(this, VistaCarnetActivity::class.java))
            R.id.nav_ajuste -> startActivity(Intent(this, AjustesActivity::class.java))
        }
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
        
        if (nombresActividades.isNotEmpty()) {
            binding.actividadDropdown.setText(nombresActividades[0], false)
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnTabSocio.setOnClickListener {
            isSocioMode = true
            updateUI()
        }

        binding.btnTabNoSocio.setOnClickListener {
            isSocioMode = false
            updateUI()
        }

        binding.etFechaNacimiento.setOnClickListener {
            showDatePicker()
        }

        binding.btnConfirmar.setOnClickListener {
            if (validateFields()) {
                saveData()
            }
        }
    }

    private fun showDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
            binding.etFechaNacimiento.setText(sdf.format(calendar.time))
        }

        DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun validateFields(): Boolean {
        var isValid = true

        if (binding.etNombre.text.isNullOrBlank()) {
            binding.etNombre.error = "El nombre es obligatorio"
            isValid = false
        }
        if (binding.etApellido.text.isNullOrBlank()) {
            binding.etApellido.error = "El apellido es obligatorio"
            isValid = false
        }
        if (binding.etDni.text.isNullOrBlank()) {
            binding.etDni.error = "El DNI es obligatorio"
            isValid = false
        }
        if (binding.etTelefono.text.isNullOrBlank()) {
            binding.etTelefono.error = "El teléfono es obligatorio"
            isValid = false
        }
        if (binding.etDireccion.text.isNullOrBlank()) {
            binding.etDireccion.error = "La dirección es obligatoria"
            isValid = false
        }
        if (binding.etFechaNacimiento.text.isNullOrBlank()) {
            binding.etFechaNacimiento.error = "La fecha de nacimiento es obligatoria"
            isValid = false
        }

        return isValid
    }

    private fun saveData() {
        val nombre = binding.etNombre.text.toString()
        val apellido = binding.etApellido.text.toString()
        val dni = binding.etDni.text.toString()
        val fechaNac = binding.etFechaNacimiento.text.toString()
        val direccion = binding.etDireccion.text.toString()
        val telefono = binding.etTelefono.text.toString()

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
                // Fechas ficticias por ahora, se podrían mejorar
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val hoy = Date()
                val proximoMes = Calendar.getInstance().apply { 
                    time = hoy
                    add(Calendar.MONTH, 1)
                }.time

                val resultado = dbHelper.registrarSocio(
                    persona = persona,
                    fechaInscripcion = sdf.format(hoy),
                    fechaVencimiento = sdf.format(proximoMes),
                    importeMensual = 15000.0
                )

                if (resultado > 0) {
                    Toast.makeText(this, "Socio registrado correctamente", Toast.LENGTH_LONG).show()
                    clearForm()
                } else {
                    Toast.makeText(this, "Error al registrar socio", Toast.LENGTH_LONG).show()
                }

            } else {
                val nombreActividadSeleccionada = binding.actividadDropdown.text.toString()
                val actividad = listaActividades.find { it.nombre == nombreActividadSeleccionada }

                if (actividad == null) {
                    Toast.makeText(this, "Por favor seleccione una actividad válida", Toast.LENGTH_LONG).show()
                    return
                }

                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val fechaHoy = sdf.format(Date())

                val carnetTemporal = dbHelper.registrarNoSocio(
                    persona = persona,
                    actividad = actividad,
                    fecha = fechaHoy
                )

                if (carnetTemporal > 0) {
                    Toast.makeText(this, "No socio registrado. Procediendo al pago...", Toast.LENGTH_LONG).show()
                    
                    // Obtener el objeto socio recién creado (o simularlo para la actividad de pagos)
                    val socioGenerado = com.example.clubdeportivo.models.Socio(
                        carnetNumero = carnetTemporal, // Usamos el carnetTemporal obtenido
                        idPersona = -1, // No lo necesitamos específicamente en PagosActivity
                        nombre = persona.nombre,
                        apellido = persona.apellido,
                        dni = persona.dni,
                        telefono = persona.telefono,
                        direccion = persona.direccion,
                        fechaNacimiento = persona.fechaNacimiento,
                        aptoFisico = persona.aptoFisico,
                        fechaInscripcion = fechaHoy,
                        activo = false,
                        vencimiento = fechaHoy
                    )
                    
                    val intent = Intent(this, PagosActivity::class.java)
                    intent.putExtra("socio", socioGenerado)
                    intent.putExtra("tipo", "NO_SOCIO")
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Error al registrar", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun clearForm() {
        binding.etNombre.text = null
        binding.etApellido.text = null
        binding.etDni.text = null
        binding.etTelefono.text = null
        binding.etDireccion.text = null
        binding.etFechaNacimiento.text = null
        binding.cbAptoFisico.isChecked = false
        
        binding.etNombre.error = null
        binding.etApellido.error = null
        binding.etDni.error = null
        binding.etTelefono.error = null
        binding.etDireccion.error = null
        binding.etFechaNacimiento.error = null
    }

    private fun hasUnsavedChanges(): Boolean {
        return !binding.etNombre.text.isNullOrBlank() ||
               !binding.etApellido.text.isNullOrBlank() ||
               !binding.etDni.text.isNullOrBlank() ||
               !binding.etTelefono.text.isNullOrBlank() ||
               !binding.etDireccion.text.isNullOrBlank() ||
               !binding.etFechaNacimiento.text.isNullOrBlank()
    }

    private fun setupBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (hasUnsavedChanges()) {
                    showExitConfirmationDialog {
                        finish()
                    }
                } else {
                    finish()
                }
            }
        })
    }

    private fun showExitConfirmationDialog(onConfirm: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("Cambios sin guardar")
            .setMessage("Hay datos cargados. ¿Estás seguro de que quieres salir?")
            .setPositiveButton("Salir") { _, _ -> onConfirm() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun updateUI() {
        val primaryColor = ContextCompat.getColor(this, R.color.primary)
        val secondaryColor = ContextCompat.getColor(this, R.color.text_secondary)
        val surfaceColor = ContextCompat.getColor(this, R.color.white)
        val activeBg = ContextCompat.getColor(this, R.color.icon_bg_blue)

        if (isSocioMode) {
            binding.btnTabSocio.apply {
                setTextColor(primaryColor)
                setBackgroundColor(activeBg)
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }
            binding.btnTabNoSocio.apply {
                setTextColor(secondaryColor)
                setBackgroundColor(surfaceColor)
                typeface = android.graphics.Typeface.DEFAULT
            }
            binding.layoutSocioExtra.visibility = View.VISIBLE
            binding.layoutNoSocioExtra.visibility = View.GONE
            binding.btnConfirmar.text = "Registrar Socio"
            binding.tvHeaderTitle.text = "Nuevo Socio"
        } else {
            binding.btnTabNoSocio.apply {
                setTextColor(primaryColor)
                setBackgroundColor(activeBg)
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }
            binding.btnTabSocio.apply {
                setTextColor(secondaryColor)
                setBackgroundColor(surfaceColor)
                typeface = android.graphics.Typeface.DEFAULT
            }
            binding.layoutSocioExtra.visibility = View.GONE
            binding.layoutNoSocioExtra.visibility = View.VISIBLE
            binding.btnConfirmar.text = "Cobrar Acceso"
            binding.tvHeaderTitle.text = "Pase Diario"
        }
    }
}