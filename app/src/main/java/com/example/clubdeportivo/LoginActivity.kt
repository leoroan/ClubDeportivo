package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubdeportivo.database.db.DbHelper
import android.widget.EditText
import android.widget.Toast
import com.example.clubdeportivo.database.usuario.UsuarioDao

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dbHelper = DbHelper(this)
        val usuarioDao = UsuarioDao(dbHelper)

        val etUser = findViewById<EditText>(R.id.etUser)
        val etPass = findViewById<EditText>(R.id.etPass)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {

            val username = etUser.text.toString().trim()
            val password = etPass.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completar campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usuario = usuarioDao.validar(username, password)

            if (usuario != null) {
                Toast.makeText(this, "Bienvenido ${usuario.nombre}!", Toast.LENGTH_SHORT).show()

                // 👉 navegar a otra pantalla
                // startActivity(Intent(this, HomeActivity::class.java))
                val intent = Intent(this, MainActivity::class.java)

                // opcional: pasar datos al intent
                intent.putExtra("username", usuario.username)
                intent.putExtra("nombre", usuario.nombre)

                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }
    }
}