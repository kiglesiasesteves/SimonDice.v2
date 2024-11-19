package com.example.simondicesecuenciacorrecta.modelView

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.simondicesecuenciacorrecta.datos.Estados
import com.example.simondicesecuenciacorrecta.datos.Ronda
import com.example.simondicesecuenciacorrecta.datos.SecuenciaJuego
import com.example.simondicesecuenciacorrecta.datos.SecuenciaJugador
import com.example.simondicesecuenciacorrecta.datos.SimonColor

class ModelView {
    val estadoLiveData: MutableLiveData<Estados> = MutableLiveData(Estados.INICIO)

    var ronda = Ronda()
    var SecuenciaJuego = SecuenciaJuego()
    var SecuenciaJugador = SecuenciaJugador()

    private fun aumentarRonda() {
        ronda.ronda.value += 1
        Log.d("ModelView", "Ronda aumentada: ${ronda.ronda.value}")
    }

    fun generarSecuencia(): List<SimonColor> {
        estadoLiveData.value = Estados.GENERANDO
        val newColor = SimonColor.entries[(0..3).random()]
        SecuenciaJuego.secuencia.add(newColor)
        estadoLiveData.value = Estados.ADIVINANDO
        Log.d("ModelView", "Nueva secuencia generada: ${SecuenciaJuego.secuencia.joinToString()}")
        return SecuenciaJuego.secuencia
    }

    fun ComprobarSecuencia():Boolean {
        val size = SecuenciaJugador.secuencia.size
        var isCorrect = true
        estadoLiveData.value = Estados.INICIO


        for (i in 0 until size) {
            if (SecuenciaJugador.secuencia[i] != SecuenciaJuego.secuencia[i]) {
                isCorrect = false
                estadoLiveData.value = Estados.INICIO
                return false

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
                estadoLiveData.value = Estados.ADIVINANDO

                return true
            }
        } else {
            return false
        }
        return false
    }

    fun clearSecuenciaJugador() {
        SecuenciaJugador.secuencia.clear()
    }

    fun clearSecuenciaJuego() {
        SecuenciaJuego.secuencia.clear()
    }
    fun clearRonda() {
        ronda.clear()
    }
}