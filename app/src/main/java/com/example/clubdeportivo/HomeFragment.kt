package com.example.clubdeportivo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.clubdeportivo.databinding.FragmentHomeBinding
import com.example.clubdeportivo.viewModels.UserViewModel

class HomeFragment : Fragment(){
    private var _binding: FragmentHomeBinding? = null
    private val userViewModel: UserViewModel by activityViewModels()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Aquí se inicializaría la lógica del fragment (listeners, etc.)
        // val nombre = arguments?.getString("nombre")

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.textView.text = "Bienvenido ${it.nombre}"
            }
        }

        // textView.text = "Bienvenido $nombre"
    }

    private fun setupListeners() {
        // binding.btnEjemplo.setOnClickListener { ... }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}