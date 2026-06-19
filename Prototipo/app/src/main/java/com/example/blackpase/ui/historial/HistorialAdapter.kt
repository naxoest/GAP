package com.example.blackpase.ui.historial

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.blackpase.R
import com.example.blackpase.data.MockData
import com.example.blackpase.model.TipoTransaccion
import com.example.blackpase.model.Transaccion

class HistorialAdapter : ListAdapter<Transaccion, HistorialAdapter.ViajeViewHolder>(ViajeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViajeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_viaje, parent, false)
        return ViajeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViajeViewHolder, position: Int) {
        val transaccion = getItem(position)
        holder.bind(transaccion)
    }

    class ViajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvLinea: TextView = itemView.findViewById(R.id.tvLinea)
        private val tvNombreLinea: TextView = itemView.findViewById(R.id.tvNombreLinea)
        private val tvTarifa: TextView = itemView.findViewById(R.id.tvTarifa)
        private val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)

        fun bind(transaccion: Transaccion) {
            if (transaccion.tipo == TipoTransaccion.CARGA) {
                tvLinea.text = "Carga de Saldo"
                tvNombreLinea.text = "Recarga +$${transaccion.monto}"
                tvTarifa.text = "+$${transaccion.monto}"
                tvTarifa.setTextColor(ContextCompat.getColor(itemView.context, R.color.success))
            } else {
                tvLinea.text = "Línea ${transaccion.linea}"
                tvNombreLinea.text = MockData.getNombreLinea(transaccion.linea)
                tvTarifa.text = "-$${transaccion.tarifa}"
                tvTarifa.setTextColor(ContextCompat.getColor(itemView.context, R.color.error))
            }
            tvFecha.text = "${transaccion.fecha} ${transaccion.hora}"
        }
    }

    class ViajeDiffCallback : DiffUtil.ItemCallback<Transaccion>() {
        override fun areItemsTheSame(oldItem: Transaccion, newItem: Transaccion): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transaccion, newItem: Transaccion): Boolean {
            return oldItem == newItem
        }
    }
}
