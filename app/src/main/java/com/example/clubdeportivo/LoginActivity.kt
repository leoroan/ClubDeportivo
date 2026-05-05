package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)
        // 1. Buscamos el botón por su ID definido en el XML
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // 2. Configuramos el click dentro de onCreate
        btnLogin.setOnClickListener {
            val intent = Intent(this, MainDashboardActivity::class.java)
            startActivity(intent)
        }
    }
}