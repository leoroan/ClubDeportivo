package com.example.clubdeportivo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment(){

//    Regla que conviene grabarme
//    Activity → Fragment → Bundle
//    Nunca depender directo de la Activity si puedo evitarlo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el layout para este fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Aquí se inicializaría la lógica del fragment (listeners, etc.)
        // val username = arguments?.getString("username")

        val nombre = arguments?.getString("nombre")

        val textView = view.findViewById<TextView>(R.id.textView)
        textView.text = "Bienvenido $nombre"
    }
}