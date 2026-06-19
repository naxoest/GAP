package com.example.blackpase.data

import com.example.blackpase.model.Transaccion
import com.example.blackpase.model.TipoCliente
import com.example.blackpase.model.TipoTransaccion
import com.example.blackpase.model.FeedbackCes

object MockData {
    var saldoActual: Int = 10000
    var tipoClienteActual: TipoCliente = TipoCliente.ADULTO

    val lineasOsorno = listOf(
        "1A" to "Av. Diego de Almagro / 5to Centenario",
        "1B" to "ULA / 5to Centenario",
        "1C" to "Tec. ULA / 5to Centenario",
        "1D" to "ULA / 5to Centenario",
        "2A" to "ULA / 5to Centenario",
        "2B" to "Tec. ULA / 5to Centenario",
        "3" to "Tec. ULA / Rahue",
        "3A" to "Tec. ULA / Rahue",
        "4" to "ULA / Rahue",
        "5" to "Los Dominicos / Ovejería",
        "5A" to "ULA / Ovejería",
        "7" to "Francke / Kolbe",
        "7A" to "Hospital - Francke / Kolbe",
        "7B" to "ULA - Francke / Kolbe"
    )

    val transacciones: MutableList<Transaccion> = mutableListOf(
        Transaccion(
            id = "1",
            fecha = "14/06/2026",
            hora = "08:30",
            tipo = TipoTransaccion.PAGO,
            linea = "1A",
            tarifa = 550,
            tipoCliente = TipoCliente.ADULTO,
            saldoRestante = 10000
        ),
        Transaccion(
            id = "2",
            fecha = "13/06/2026",
            hora = "17:45",
            tipo = TipoTransaccion.PAGO,
            linea = "5",
            tarifa = 550,
            tipoCliente = TipoCliente.ADULTO,
            saldoRestante = 10550
        ),
        Transaccion(
            id = "3",
            fecha = "12/06/2026",
            hora = "12:15",
            tipo = TipoTransaccion.PAGO,
            linea = "7B",
            tarifa = 550,
            tipoCliente = TipoCliente.ADULTO,
            saldoRestante = 11100
        )
    )

    val feedbacks: MutableList<FeedbackCes> = mutableListOf()

    fun agregarTransaccion(transaccion: Transaccion) {
        transacciones.add(0, transaccion)
        saldoActual = transaccion.saldoRestante
    }

    fun agregarFeedback(feedback: FeedbackCes) {
        feedbacks.add(0, feedback)
    }

    fun getPromedioCes(): Float {
        if (feedbacks.isEmpty()) return 0f
        return feedbacks.map { it.puntuacion }.average().toFloat()
    }

    fun getCantidadPorPuntuacion(): Map<Int, Int> {
        return feedbacks.groupBy { it.puntuacion }.mapValues { it.value.size }
    }

    fun tieneSaldoSuficiente(): Boolean {
        return saldoActual >= tipoClienteActual.tarifa
    }

    fun generarId(): String {
        return System.currentTimeMillis().toString()
    }

    fun getNombreLinea(linea: String): String {
        return lineasOsorno.find { it.first == linea }?.second ?: "Línea $linea"
    }
}
