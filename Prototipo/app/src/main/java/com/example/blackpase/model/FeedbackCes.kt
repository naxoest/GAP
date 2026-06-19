package com.example.blackpase.model

data class FeedbackCes(
    val id: String,
    val fecha: String,
    val hora: String,
    val puntuacion: Int,
    val comentario: String = "",
    val tipoCliente: String = "",
    val linea: String = ""
)
