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

    private val _viajesPosibles = MutableLiveData<Int>()
    val viajesPosibles: LiveData<Int> = _viajesPosibles

    private val _bajoUmbral = MutableLiveData<Boolean>()
    val bajoUmbral: LiveData<Boolean> = _bajoUmbral

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        _saldo.value = MockData.saldoActual
        _tipoCliente.value = MockData.tipoClienteActual
        _tarifa.value = MockData.tipoClienteActual.tarifa
        _ultimosViajes.value = MockData.transacciones.take(3)
        calcularViajesPosibles()
    }

    fun calcularViajesPosibles() {
        val tarifa = MockData.tipoClienteActual.tarifa
        val saldo = MockData.saldoActual
        _viajesPosibles.value = if (tarifa > 0) saldo / tarifa else 0
        _bajoUmbral.value = (saldo / tarifa) < 1
    }

    fun actualizarTipoCliente(tipo: TipoCliente) {
        MockData.tipoClienteActual = tipo
        _tipoCliente.value = tipo
        _tarifa.value = tipo.tarifa
        calcularViajesPosibles()
    }
}
