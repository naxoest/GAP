package com.example.blackpase.ui.saldo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blackpase.data.MockData
import com.example.blackpase.model.TipoCliente
import com.example.blackpase.model.Transaccion

class SaldoViewModel : ViewModel() {

    private val _saldo = MutableLiveData<Int>()
    val saldo: LiveData<Int> = _saldo

    private val _tipoCliente = MutableLiveData<TipoCliente>()
    val tipoCliente: LiveData<TipoCliente> = _tipoCliente

    private val _tarifa = MutableLiveData<Int>()
    val tarifa: LiveData<Int> = _tarifa

    private val _ultimosViajes = MutableLiveData<List<Transaccion>>()
    val ultimosViajes: LiveData<List<Transaccion>> = _ultimosViajes

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        _saldo.value = MockData.saldoActual
        _tipoCliente.value = MockData.tipoClienteActual
        _tarifa.value = MockData.tipoClienteActual.tarifa
        _ultimosViajes.value = MockData.transacciones.take(3)
    }

    fun actualizarTipoCliente(tipo: TipoCliente) {
        MockData.tipoClienteActual = tipo
        _tipoCliente.value = tipo
        _tarifa.value = tipo.tarifa
    }
}
