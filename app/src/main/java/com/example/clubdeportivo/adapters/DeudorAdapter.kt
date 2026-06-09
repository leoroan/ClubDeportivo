package com.example.clubdeportivo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clubdeportivo.R

class DeudorAdapter(private val deudores: List<Map<String, Any>>) :
    RecyclerView.Adapter<DeudorAdapter.DeudorViewHolder>() {

    class DeudorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreDeudor)
        val tvDetalles: TextView = view.findViewById(R.id.tvDetallesDeudor)
        val tvMonto: TextView = view.findViewById(R.id.tvMontoDeudor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeudorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_deudor, parent, false)
        return DeudorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeudorViewHolder, position: Int) {
        val deudor = deudores[position]
        holder.tvNombre.text = deudor["nombre"].toString()
        holder.tvDetalles.text = deudor["detalles"].toString()
        holder.tvMonto.text = "$ ${deudor["monto"]}"
    }

    override fun getItemCount() = deudores.size
}