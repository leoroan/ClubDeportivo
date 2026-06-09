package com.example.clubdeportivo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clubdeportivo.R
import com.example.clubdeportivo.models.Socio

class PersonaAdapter(
    private var personas: List<Pair<String, Socio>>,
    private val onItemClick: (Pair<String, Socio>) -> Unit
) : RecyclerView.Adapter<PersonaAdapter.PersonaViewHolder>() {

    class PersonaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombrePersona)
        val tvDetalle: TextView = view.findViewById(R.id.tvDetallePersona)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_persona, parent, false)
        return PersonaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonaViewHolder, position: Int) {
        val item = personas[position]
        val tipo = item.first
        val s = item.second
        val prefijo = if (tipo == "SOCIO") "S" else "NS"
        
        holder.tvNombre.text = "${s.nombre} ${s.apellido}"
        holder.tvDetalle.text = "N° $prefijo-${s.carnetNumero} • DNI: ${s.dni}"
        
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount() = personas.size

    fun updateData(newPersonas: List<Pair<String, Socio>>) {
        personas = newPersonas
        notifyDataSetChanged()
    }
}
