package com.example.clubdeportivo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.clubdeportivo.databinding.ActivityMainBinding

class MainDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
