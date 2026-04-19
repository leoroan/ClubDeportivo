package com.example.clubdeportivo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubdeportivo.databinding.ActivityMainBinding
import com.example.clubdeportivo.models.Usuario
import com.example.clubdeportivo.viewModels.UserViewModel

class MainDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        enableEdgeToEdge()

        // 1. Configurar Insets para que no se superponga con el sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        // 2. Recuperar el usuario del intent y guardarlo en el ViewModel
        val usuario = intent.getSerializableExtra("user") as? Usuario
        usuario?.let {
            userViewModel.setUser(it)
        }

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        // Observar el usuario para actualizar el saludo
        userViewModel.user.observe(this) { user ->
            binding.greetingText.text = "Hola, ${user?.nombre ?: "Usuario"}"
        }

        // Configurar el BottomNav (Seleccionar Inicio por defecto)
        binding.bottomNav.selectedItemId = R.id.homeFragment
    }

    private fun setupListeners() {
        // Acciones de las Cards del Dashboard
        binding.cardNuevoSocio.setOnClickListener {
            Toast.makeText(this, "Navegando a Registro de Socio", Toast.LENGTH_SHORT).show()
            // Aquí iría tu lógica de navegación, ej: startActivity(...)
        }

        binding.cardCobrarPago.setOnClickListener {
            Toast.makeText(this, "Navegando a Cobrar Pago", Toast.LENGTH_SHORT).show()
        }

        binding.cardNuevoNosocio.setOnClickListener {
            Toast.makeText(this, "Navegando a Nuevo No-Socio", Toast.LENGTH_SHORT).show()
        }

        binding.cardGenerarCarnet.setOnClickListener {
            Toast.makeText(this, "Navegando a Generar Carnet", Toast.LENGTH_SHORT).show()
        }

        // Acciones del Bottom Navigation
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    Toast.makeText(this, "Ya estás en Inicio", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.registroFragment -> {
                    Toast.makeText(this, "Abriendo Registro", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menuFragment -> {
                    Toast.makeText(this, "Abriendo Menú", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}
