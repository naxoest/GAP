package com.example.blackpase.ui.chofer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blackpase.data.MockData
import com.example.blackpase.model.TipoTransaccion
import com.example.blackpase.model.Transaccion

class ChoferViewModel : ViewModel() {

    private val _totalRecaudado = MutableLiveData<Int>()
    val totalRecaudado: LiveData<Int> = _totalRecaudado

    private val _cantidadPasajeros = MutableLiveData<Int>()
    val cantidadPasajeros: LiveData<Int> = _cantidadPasajeros

    private val _tiempoPromedio = MutableLiveData<String>()
    val tiempoPromedio: LiveData<String> = _tiempoPromedio

    private val _pagosRecientes = MutableLiveData<List<Transaccion>>()
    val pagosRecientes: LiveData<List<Transaccion>> = _pagosRecientes

    private val _duracionRecorrido = MutableLiveData<String>()
    val duracionRecorrido: LiveData<String> = _duracionRecorrido

    private var lineaFiltrada: String? = null

    fun cargarDatos() {
        val sesion = MockData.sesionChoferActual
        val pagos = MockData.transacciones.filter {
            it.tipo == TipoTransaccion.PAGO &&
                    it.fecha == sesion?.fechaInicio &&
                    (lineaFiltrada == null || it.linea == lineaFiltrada)
        }

        _pagosRecientes.value = pagos
        _cantidadPasajeros.value = pagos.size
        _totalRecaudado.value = pagos.sumOf { it.tarifa }

        if (sesion != null) {
            _duracionRecorrido.value = calcularDuracion(sesion.horaInicio)
        } else {
            _duracionRecorrido.value = "0 min"
        }

        if (pagos.size > 1) {
            _tiempoPromedio.value = "Cada ~${2 + (Math.random() * 3).toInt()} min"
        } else {
            _tiempoPromedio.value = "--"
        }
    }

    fun filtrarPorLinea(linea: String?) {
        lineaFiltrada = linea
        cargarDatos()
    }

    private fun calcularDuracion(horaInicio: String): String {
        try {
            val partes = horaInicio.split(":")
            val horaInicioMin = partes[0].toInt() * 60 + partes[1].toInt()
            val ahora = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
                .format(java.util.Date())
            val partesAhora = ahora.split(":")
            val ahoraMin = partesAhora[0].toInt() * 60 + partesAhora[1].toInt()
            val diff = ahoraMin - horaInicioMin
            if (diff < 0) return "0 min"
            val hrs = diff / 60
            val min = diff % 60
            return if (hrs > 0) "${hrs}h ${min}min" else "${min} min"
        } catch (e: Exception) {
            return "0 min"
        }
    }
}
