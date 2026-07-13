package com.example.blackpase.ui.historial

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blackpase.R
import com.example.blackpase.data.MockData
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class HistorialFragment : Fragment() {

    private lateinit var historialViewModel: HistorialViewModel
    private lateinit var historialAdapter: HistorialAdapter
    private val lineasSeleccionadas = mutableSetOf<String>()
    private val MAX_LINEAS = 4

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
        val btnSeleccionarLineas = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSeleccionarLineas)
        val chipGroupLineasSeleccionadas = view.findViewById<ChipGroup>(R.id.chipGroupLineasSeleccionadas)
        val layoutLineasSeleccionadas = view.findViewById<LinearLayout>(R.id.layoutLineasSeleccionadas)
        val btnClearLineas = view.findViewById<ImageButton>(R.id.btnClearLineas)

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

        btnSeleccionarLineas.setOnClickListener {
            mostrarDialogoSeleccionLineas()
        }

        btnClearLineas.setOnClickListener {
            lineasSeleccionadas.clear()
            chipGroupLineasSeleccionadas.removeAllViews()
            layoutLineasSeleccionadas.visibility = View.GONE
            historialViewModel.filtrarPorLinea(emptyList())
        }

        historialViewModel.transacciones.observe(viewLifecycleOwner) { transacciones ->
            historialAdapter.submitList(transacciones)
        }

        btnMetricas.setOnClickListener {
            mostrarMetricas()
        }
    }

    private fun mostrarDialogoSeleccionLineas() {
        val lineasDisponibles = MockData.lineasOsorno.map { it.first }
        val tempSeleccionadas = lineasSeleccionadas.toMutableSet()

        val adapter = object : android.widget.BaseAdapter() {
            override fun getCount() = lineasDisponibles.size
            override fun getItem(position: Int) = lineasDisponibles[position]
            override fun getItemId(position: Int) = position.toLong()

            override fun getView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(requireContext())
                    .inflate(android.R.layout.simple_list_item_multiple_choice, parent, false)
                val checkedTextView = view as android.widget.CheckedTextView
                checkedTextView.text = "Línea ${lineasDisponibles[position]}"
                checkedTextView.isChecked = lineasDisponibles[position] in tempSeleccionadas
                return view
            }
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Seleccionar líneas (máx. $MAX_LINEAS)")
            .setAdapter(adapter, null)
            .setPositiveButton("Aceptar") { _, _ ->
                lineasSeleccionadas.clear()
                lineasSeleccionadas.addAll(tempSeleccionadas)
                actualizarChipsLineas()
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.listView.setOnItemClickListener { _, view, position, _ ->
            val linea = lineasDisponibles[position]
            val checkedTextView = view as android.widget.CheckedTextView

            if (linea in tempSeleccionadas) {
                tempSeleccionadas.remove(linea)
                checkedTextView.isChecked = false
            } else {
                if (tempSeleccionadas.size >= MAX_LINEAS) {
                    Toast.makeText(requireContext(), "Máximo $MAX_LINEAS líneas", Toast.LENGTH_SHORT).show()
                    return@setOnItemClickListener
                }
                tempSeleccionadas.add(linea)
                checkedTextView.isChecked = true
            }
        }

        dialog.show()
    }

    private fun actualizarChipsLineas() {
        val chipGroup = view?.findViewById<ChipGroup>(R.id.chipGroupLineasSeleccionadas) ?: return
        val layout = view?.findViewById<LinearLayout>(R.id.layoutLineasSeleccionadas) ?: return

        chipGroup.removeAllViews()

        if (lineasSeleccionadas.isEmpty()) {
            layout.visibility = View.GONE
            historialViewModel.filtrarPorLinea(emptyList())
            return
        }

        layout.visibility = View.VISIBLE

        for (linea in lineasSeleccionadas) {
            val chip = Chip(requireContext()).apply {
                text = linea
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    lineasSeleccionadas.remove(linea)
                    actualizarChipsLineas()
                }
            }
            chipGroup.addView(chip)
        }

        historialViewModel.filtrarPorLinea(lineasSeleccionadas.toList())
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
