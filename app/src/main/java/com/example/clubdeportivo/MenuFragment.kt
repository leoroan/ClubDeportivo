package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.clubdeportivo.databinding.FragmentMenuBinding
import com.example.clubdeportivo.viewModels.UserViewModel

class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observar el usuario desde el ViewModel compartido con la Activity
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                setupUserHeader(it.nombre, it.rol ?: "Administrador")
            }
        }

        // 2) Configuración de Click Listeners (Hooks de navegación)
        setupClickListeners()
    }

    private fun setupUserHeader(nombre: String, rol: String) {
        binding.tvUserName.text = nombre
        binding.tvUserRole.text = rol
    }

    private fun setupClickListeners() {
        binding.cardDeudores.setOnClickListener {
            // TODO: Navegar a Fragment de Deudores
            showToast("Abriendo listado de deudores...")
        }

        binding.cardAcademica.setOnClickListener {
            // TODO: Navegar a Gestión Académica
            showToast("Abriendo gestión académica...")
        }

        binding.cardPerfil.setOnClickListener {
            // TODO: Navegar a Mi Perfil
            showToast("Abriendo mi perfil...")
        }

        binding.cardLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        // Limpio el usuario en el ViewModel compartido
        userViewModel.clearUser()

        showToast("Sesión cerrada correctamente")

        // Para navegar de regreso al Login y limpiar el stack de actividades para que no se pueda volver atrás
        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        requireActivity().finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
