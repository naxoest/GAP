package com.example.blackpase.ui.chofer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.blackpase.R
import com.example.blackpase.data.MockData

class DashboardChoferFragment : Fragment() {

    private lateinit var choferViewModel: ChoferViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard_chofer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        choferViewModel = ViewModelProvider(requireActivity()).get(ChoferViewModel::class.java)

        val tvTotalRecaudado = view.findViewById<TextView>(R.id.tvTotalRecaudado)
        val tvCantidadPasajeros = view.findViewById<TextView>(R.id.tvCantidadPasajeros)
        val tvTiempoPromedio = view.findViewById<TextView>(R.id.tvTiempoPromedio)
        val tvDuracionRecorrido = view.findViewById<TextView>(R.id.tvDuracionRecorrido)
        val tvLineaChofer = view.findViewById<TextView>(R.id.tvLineaChofer)
        val tvResumenViaje = view.findViewById<TextView>(R.id.tvResumenViaje)
        val spinnerLineaChofer = view.findViewById<Spinner>(R.id.spinnerLineaChofer)

        val sesion = MockData.sesionChoferActual
        val lineas = MockData.lineasOsorno.map { it.first }
        val nombresLineas = MockData.lineasOsorno.map { "${it.first} - ${it.second}" }

        val adapterLineas = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombresLineas)
        adapterLineas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLineaChofer.adapter = adapterLineas

        val lineaInicial = sesion?.linea ?: lineas.firstOrNull() ?: ""
        val indiceInicial = lineas.indexOf(lineaInicial)
        if (indiceInicial >= 0) {
            spinnerLineaChofer.setSelection(indiceInicial)
        }

        spinnerLineaChofer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val lineaSeleccionada = lineas[position]
                tvLineaChofer.text = lineaSeleccionada
                MockData.sesionChoferActual = sesion?.copy(linea = lineaSeleccionada)
                choferViewModel.filtrarPorLinea(lineaSeleccionada)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        choferViewModel.totalRecaudado.observe(viewLifecycleOwner) { total ->
            tvTotalRecaudado.text = "$${String.format("%,d", total).replace(",", ".")}"
        }

        choferViewModel.cantidadPasajeros.observe(viewLifecycleOwner) { cantidad ->
            tvCantidadPasajeros.text = cantidad.toString()
        }

        choferViewModel.tiempoPromedio.observe(viewLifecycleOwner) { tiempo ->
            tvTiempoPromedio.text = tiempo
        }

        choferViewModel.duracionRecorrido.observe(viewLifecycleOwner) { duracion ->
            tvDuracionRecorrido.text = duracion
        }

        choferViewModel.pagosRecientes.observe(viewLifecycleOwner) { pagos ->
            val total = pagos.sumOf { it.tarifa }
            val count = pagos.size
            tvResumenViaje.text = "Recorrido en curso\n" +
                    "Pagos registrados: $count\n" +
                    "Total: $${String.format("%,d", total).replace(",", ".")}"
        }
    }

    override fun onResume() {
        super.onResume()
        choferViewModel.cargarDatos()
    }
}
