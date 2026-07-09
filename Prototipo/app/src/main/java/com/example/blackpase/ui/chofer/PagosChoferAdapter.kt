package com.example.blackpase.ui.chofer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.blackpase.R
import com.example.blackpase.data.MockData
import com.example.blackpase.model.Transaccion

class PagosChoferAdapter : ListAdapter<Transaccion, PagosChoferAdapter.PagoViewHolder>(PagoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pago_chofer, parent, false)
        return PagoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagoViewHolder, position: Int) {
        val transaccion = getItem(position)
        holder.bind(transaccion)
    }

    class PagoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvHoraPago: TextView = itemView.findViewById(R.id.tvHoraPago)
        private val tvLineaPago: TextView = itemView.findViewById(R.id.tvLineaPago)
        private val tvTipoClientePago: TextView = itemView.findViewById(R.id.tvTipoClientePago)
        private val tvMontoPago: TextView = itemView.findViewById(R.id.tvMontoPago)
        private val tvSaldoRestante: TextView = itemView.findViewById(R.id.tvSaldoRestante)

        fun bind(transaccion: Transaccion) {
            tvHoraPago.text = transaccion.hora
            tvLineaPago.text = "Línea ${transaccion.linea} - ${MockData.getNombreLinea(transaccion.linea)}"
            tvTipoClientePago.text = transaccion.tipoCliente.displayName
            tvMontoPago.text = "+$${transaccion.tarifa}"
            tvSaldoRestante.visibility = View.GONE
        }
    }

    class PagoDiffCallback : DiffUtil.ItemCallback<Transaccion>() {
        override fun areItemsTheSame(oldItem: Transaccion, newItem: Transaccion): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transaccion, newItem: Transaccion): Boolean {
            return oldItem == newItem
        }
    }
}
