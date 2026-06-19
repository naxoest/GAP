package com.example.blackpase.model

enum class TipoCliente(val tarifa: Int, val displayName: String) {
    ADULTO(550, "Adulto"),
    ADULTO_MAYOR(270, "Adulto Mayor"),
    ESTUDIANTE(180, "Estudiante")
}
