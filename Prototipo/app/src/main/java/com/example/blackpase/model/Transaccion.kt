package com.example.blackpase.model

enum class TipoTransaccion {
    PAGO,
    CARGA
}

data class Transaccion(
    val id: String,
    val fecha: String,
    val hora: String,
    val tipo: TipoTransaccion,
    val linea: String = "",
    val tarifa: Int = 0,
    val monto: Int = 0,
    val tipoCliente: TipoCliente = TipoCliente.ADULTO,
    val saldoRestante: Int
)
