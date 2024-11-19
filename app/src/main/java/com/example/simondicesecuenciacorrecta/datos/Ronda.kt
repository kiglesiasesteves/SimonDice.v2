package com.example.simondicesecuenciacorrecta.datos

import androidx.compose.runtime.mutableStateOf

class Ronda {
    var ronda = mutableStateOf(0)

    fun clear() {
        ronda.value = 0
    }

}