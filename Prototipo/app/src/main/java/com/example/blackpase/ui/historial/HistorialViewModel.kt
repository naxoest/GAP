package com.example.blackpase.ui.historial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blackpase.data.MockData
import com.example.blackpase.model.Transaccion
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HistorialViewModel : ViewModel() {

    private val _transacciones = MutableLiveData<List<Transaccion>>()
    val transacciones: LiveData<List<Transaccion>> = _transacciones

    private val _filtroActual = MutableLiveData<String>()
    val filtroActual: LiveData<String> = _filtroActual

    private val _filtroLinea = MutableLiveData<List<String>>(emptyList())
    val filtroLinea: LiveData<List<String>> = _filtroLinea

    init {
        _filtroActual.value = "Todos"
        cargarTransacciones("Todos")
    }

    fun cargarTransacciones(filtro: String) {
        _filtroActual.value = filtro
        aplicarFiltros()
    }

    fun filtrarPorLinea(lineas: List<String>) {
        _filtroLinea.value = lineas
        aplicarFiltros()
    }

    private fun aplicarFiltros() {
        var lista: List<Transaccion> = MockData.transacciones

        when (_filtroActual.value) {
            "Hoy" -> {
                val hoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                lista = lista.filter { it.fecha == hoy }
            }
            "Esta Semana" -> {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, -7)
                val fechaLimite = sdf.format(calendar.time)
                lista = lista.filter { it.fecha >= fechaLimite }
            }
        }

        val lineasSeleccionadas = _filtroLinea.value
        if (!lineasSeleccionadas.isNullOrEmpty()) {
            lista = lista.filter { it.linea in lineasSeleccionadas }
        }

        _transacciones.value = lista
    }
}
