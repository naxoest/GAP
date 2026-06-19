package com.example.blackpase.ui.historial

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blackpase.R
import com.example.blackpase.data.MockData
import com.google.android.material.chip.ChipGroup

class HistorialFragment : Fragment() {

    private lateinit var historialViewModel: HistorialViewModel
    private lateinit var historialAdapter: HistorialAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_historial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historialViewModel = ViewModelProvider(this).get(HistorialViewModel::class.java)

        val rvHistorial = view.findViewById<RecyclerView>(R.id.rvHistorial)
        val chipGroupFiltros = view.findViewById<ChipGroup>(R.id.chipGroupFiltros)
        val btnMetricas = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnMetricas)

        historialAdapter = HistorialAdapter()
        rvHistorial.layoutManager = LinearLayoutManager(requireContext())
        rvHistorial.adapter = historialAdapter

        chipGroupFiltros.setOnCheckedStateChangeListener { _, checkedIds ->
            val filtro = when {
                checkedIds.contains(R.id.chipHoy) -> "Hoy"
                checkedIds.contains(R.id.chipSemana) -> "Esta Semana"
                else -> "Todos"
            }
            historialViewModel.cargarTransacciones(filtro)
        }

        historialViewModel.transacciones.observe(viewLifecycleOwner) { transacciones ->
            historialAdapter.submitList(transacciones)
        }

        btnMetricas.setOnClickListener {
            mostrarMetricas()
        }
    }

    private fun mostrarMetricas() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_metricas, null)

        val tvTotalEncuestas = dialogView.findViewById<TextView>(R.id.tvTotalEncuestas)
        val tvPromedioCes = dialogView.findViewById<TextView>(R.id.tvPromedioCes)
        val tvInterpretacion = dialogView.findViewById<TextView>(R.id.tvInterpretacion)
        val layoutBarras = dialogView.findViewById<LinearLayout>(R.id.layoutBarras)
        val layoutRespuestas = dialogView.findViewById<LinearLayout>(R.id.layoutRespuestas)
        val tvIteracion = dialogView.findViewById<TextView>(R.id.tvIteracion)

        val feedbacks = MockData.feedbacks
        val total = feedbacks.size
        val promedio = MockData.getPromedioCes()

        tvTotalEncuestas.text = total.toString()
        tvPromedioCes.text = String.format("%.1f", promedio)

        tvInterpretacion.text = when {
            promedio >= 4.5 -> "Muy fácil de usar"
            promedio >= 3.5 -> "Fácil de usar"
            promedio >= 2.5 -> "Neutral"
            promedio >= 1.5 -> "Difícil de usar"
            promedio > 0 -> "Muy difícil de usar"
            else -> "Sin datos"
        }

        tvIteracion.text = when {
            promedio < 3 -> "CES bajo (< 3): Se propone simplificar el flujo de pago eliminando pasos innecesarios y agregando un botón de pago rápido."
            promedio < 4 -> "CES medio (3-4): Se propone agregar ayuda contextual y mejorar la visibilidad del saldo disponible."
            else -> "CES alto (≥ 4): El flujo es satisfactorio. Se propone agregar función de recarga rápida desde la pantalla de pago."
        }

        layoutBarras.removeAllViews()
        for (i in 1..5) {
            val cantidad = MockData.getCantidadPorPuntuacion()[i] ?: 0
            val barraView = LayoutInflater.from(requireContext()).inflate(R.layout.item_barra_metrica, layoutBarras, false)
            barraView.findViewById<TextView>(R.id.tvEtiqueta).text = "${i}★"
            barraView.findViewById<TextView>(R.id.tvCantidad).text = cantidad.toString()

            val barra = barraView.findViewById<View>(R.id.barra)
            val params = barra.layoutParams
            val maxWidthPx = resources.displayMetrics.widthPixels - 200
            params.width = if (total > 0) (cantidad * maxWidthPx) / total else 0
            barra.layoutParams = params

            layoutBarras.addView(barraView)
        }

        layoutRespuestas.removeAllViews()
        val mostrarFeedbacks = feedbacks.take(5)
        for (feedback in mostrarFeedbacks) {
            val respuestaView = LayoutInflater.from(requireContext()).inflate(R.layout.item_respuesta_metrica, layoutRespuestas, false)
            respuestaView.findViewById<RatingBar>(R.id.ratingBarRespuesta).rating = feedback.puntuacion.toFloat()
            val tvTipoClienteLinea = respuestaView.findViewById<TextView>(R.id.tvTipoClienteLinea)
            val infoParts = mutableListOf<String>()
            if (feedback.tipoCliente.isNotEmpty()) infoParts.add(feedback.tipoCliente)
            if (feedback.linea.isNotEmpty()) infoParts.add("Línea ${feedback.linea}")
            tvTipoClienteLinea.text = infoParts.joinToString(" • ")
            val tvComentario = respuestaView.findViewById<TextView>(R.id.tvComentarioRespuesta)
            if (feedback.comentario.isNotEmpty()) {
                tvComentario.text = feedback.comentario
            } else {
                tvComentario.text = "Sin comentario"
            }
            layoutRespuestas.addView(respuestaView)
        }

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Cerrar", null)
            .show()
    }
}
