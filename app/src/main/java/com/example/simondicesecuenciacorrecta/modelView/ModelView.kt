package com.example.simondicesecuenciacorrecta.modelView

import android.util.Log
import com.example.simondicesecuenciacorrecta.datos.Ronda
import com.example.simondicesecuenciacorrecta.datos.SecuenciaJuego
import com.example.simondicesecuenciacorrecta.datos.SecuenciaJugador
import com.example.simondicesecuenciacorrecta.datos.SimonColor

class ModelView {

    var ronda = Ronda()
    var SecuenciaJuego = SecuenciaJuego()
    var SecuenciaJugador = SecuenciaJugador()

    private fun aumentarRonda() {
        ronda.ronda.value += 1
        Log.d("ModelView", "Ronda aumentada: ${ronda.ronda.value}")
    }

    fun generarSecuencia() {

        val newColor = SimonColor.entries[(0..3).random()]
        SecuenciaJuego.secuencia.add(newColor)
        Log.d("ModelView", "Nueva secuencia generada: ${SecuenciaJuego.secuencia.joinToString()}")
    }

    fun ComprobarSecuencia() {
        val size = SecuenciaJugador.secuencia.size
        var isCorrect = true

        for (i in 0 until size) {
            if (SecuenciaJugador.secuencia[i] != SecuenciaJuego.secuencia[i]) {
                isCorrect = false
                break
            }
        }

        Log.d("ModelView", "Secuencia del jugador: ${SecuenciaJugador.secuencia.joinToString()}")
        Log.d("ModelView", "Secuencia del juego: ${SecuenciaJuego.secuencia.joinToString()}")
        Log.d("ModelView", "Secuencia correcta: $isCorrect")

        if (isCorrect) {
            if (size == SecuenciaJuego.secuencia.size) {
                Log.d("Secuencia", "Secuencia correcta")
                aumentarRonda()
                generarSecuencia()
                clearSecuenciaJugador()
            }
        } else {
        }
    }

    fun clearSecuenciaJugador() {
        SecuenciaJugador.secuencia.clear()
    }

    fun clearSecuenciaJuego() {
        SecuenciaJuego.secuencia.clear()
    }
}