package com.example.simondicesecuenciacorrecta.datos

import androidx.compose.runtime.mutableStateListOf

data class SecuenciaJuego(
    val secuencia: MutableList<SimonColor> = mutableStateListOf()
)