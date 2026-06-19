package com.example.blackpase.ui.historial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blackpase.data.MockData
import com.example.blackpase.model.Transaccion

class HistorialViewModel : ViewModel() {

    private val _transacciones = MutableLiveData<List<Transaccion>>()
    val transacciones: LiveData<List<Transaccion>> = _transacciones

    private val _filtroActual = MutableLiveData<String>()
    val filtroActual: LiveData<String> = _filtroActual

    init {
        _filtroActual.value = "Todos"
        cargarTransacciones("Todos")
    }

    fun cargarTransacciones(filtro: String) {
        _filtroActual.value = filtro
        val listaCompleta = MockData.transacciones

        when (filtro) {
            "Hoy" -> {
                val hoy = "17/06/2026"
                _transacciones.value = listaCompleta.filter { it.fecha == hoy }
            }
            "Esta Semana" -> {
                _transacciones.value = listaCompleta
            }
            else -> {
                _transacciones.value = listaCompleta
            }
        }
    }
}
