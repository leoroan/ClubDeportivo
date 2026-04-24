package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class VistaCarnetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_carnet)

        // 1. Buscamos el botón por su ID definido en el XML
        val btncarnet = findViewById<LinearLayout>(R.id.btncarnet)
        val btncarnet_no_socio = findViewById<LinearLayout>(R.id.btncarnet_no_socio)

        // 2. Configuramos el click dentro de onCreate
        btncarnet.setOnClickListener {
            val intent = Intent(this, CarnetSocioActivity::class.java)
            startActivity(intent)
        }
        btncarnet_no_socio.setOnClickListener {
            val intent = Intent(this, CarnetNoSocioActivity::class.java)
            startActivity(intent)
        }

    }
}