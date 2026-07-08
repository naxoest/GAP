package com.example.blackpase.model

data class SesionChofer(
    val linea: String,
    val horaInicio: String,
    val fechaInicio: String
)

data class Comercio(
    val id: String,
    val nombre: String,
    val comision: Float
)
