package com.example.clubdeportivo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.clubdeportivo.database.db.DbHelper
import com.example.clubdeportivo.database.usuario.UsuarioDao
import com.example.clubdeportivo.databinding.FragmentRegistroBinding

class RegistroFragment : Fragment() {

    private var _binding: FragmentRegistroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnConfirmarRegistro.setOnClickListener {
            validarYRegistrar()
        }
    }

    private fun validarYRegistrar() {
        val nombre = binding.etNombre.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val rol = "usuario"

        // 1. Validaciones básicas
        if (nombre.isEmpty()) {
            binding.tilNombre.error = "El nombre es obligatorio"
            return
        } else {
            binding.tilNombre.error = null
        }

        if (username.length < 4) {
            binding.tilUsername.error = "El usuario debe tener al menos 4 caracteres"
            return
        } else {
            binding.tilUsername.error = null
        }

        if (password.length < 6) {
            binding.tilPassword.error = "La contraseña debe tener al menos 6 caracteres"
            return
        } else {
            binding.tilPassword.error = null
        }

        // 2. Persistencia en Base de Datos
        val dbHelper = DbHelper(requireContext())
        val usuarioDao = UsuarioDao(dbHelper)

        val resultado = usuarioDao.insertar(nombre, username, password, rol)

        if (resultado != -1L) {
            Toast.makeText(requireContext(), "¡Registro exitoso!", Toast.LENGTH_SHORT).show()
            limpiarCampos()
            // Opcional: navegar hacia atrás o al login
            // findNavController().popBackStack()
        } else {
            Toast.makeText(requireContext(), "Error al registrar. El usuario podría ya existir.", Toast.LENGTH_LONG).show()
        }
    }

    private fun limpiarCampos() {
        binding.etNombre.text?.clear()
        binding.etUsername.text?.clear()
        binding.etPassword.text?.clear()
        binding.tilNombre.error = null
        binding.tilUsername.error = null
        binding.tilPassword.error = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
