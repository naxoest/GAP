package com.example.blackpase.ui.pagar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blackpase.data.MockData
import com.example.blackpase.model.QRRuta
import com.example.blackpase.model.TipoCliente
import com.example.blackpase.model.TipoTransaccion
import com.example.blackpase.model.Transaccion
import com.google.gson.Gson

class PagarViewModel : ViewModel() {

    private val _saldoActual = MutableLiveData<Int>()
    val saldoActual: LiveData<Int> = _saldoActual

    private val _tipoCliente = MutableLiveData<TipoCliente>()
    val tipoCliente: LiveData<TipoCliente> = _tipoCliente

    private val _pagoExitoso = MutableLiveData<Boolean>()
    val pagoExitoso: LiveData<Boolean> = _pagoExitoso

    private val _mensajeError = MutableLiveData<String>()
    val mensajeError: LiveData<String> = _mensajeError

    private val _transaccionCreada = MutableLiveData<Transaccion?>()
    val transaccionCreada: LiveData<Transaccion?> = _transaccionCreada

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        _saldoActual.value = MockData.saldoActual
        _tipoCliente.value = MockData.tipoClienteActual
    }

    fun procesarQR(qrData: String) {
        try {
            val gson = Gson()
            val qrRuta = gson.fromJson(qrData, QRRuta::class.java)

            if (qrRuta.linea.isNullOrBlank()) {
                _mensajeError.value = "Código QR inválido: falta número de línea"
                _pagoExitoso.value = false
                return
            }

            if (!MockData.tieneSaldoSuficiente()) {
                _mensajeError.value = "Saldo insuficiente para este viaje"
                _pagoExitoso.value = false
                return
            }

            val tarifa = MockData.tipoClienteActual.tarifa
            val nuevoSaldo = MockData.saldoActual - tarifa

            val transaccion = Transaccion(
                id = MockData.generarId(),
                fecha = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date()),
                hora = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date()),
                tipo = TipoTransaccion.PAGO,
                linea = qrRuta.linea,
                tarifa = tarifa,
                tipoCliente = MockData.tipoClienteActual,
                saldoRestante = nuevoSaldo
            )

            MockData.agregarTransaccion(transaccion)
            _saldoActual.value = nuevoSaldo
            _pagoExitoso.value = true
            _transaccionCreada.value = transaccion

        } catch (e: Exception) {
            _mensajeError.value = "Error al procesar el código QR"
            _pagoExitoso.value = false
        }
    }

    fun reiniciarEstado() {
        _pagoExitoso.value = false
        _transaccionCreada.value = null
    }
}
