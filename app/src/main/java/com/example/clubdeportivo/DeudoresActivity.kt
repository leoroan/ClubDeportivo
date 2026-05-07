package com.example.clubdeportivo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubdeportivo.databinding.ActivityDeudoresBinding

class DeudoresActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeudoresBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeudoresBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener {
            Toast.makeText(this, "Regresando al perfil", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
