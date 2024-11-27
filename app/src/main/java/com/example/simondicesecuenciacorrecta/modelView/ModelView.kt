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

    /**
     * Genera una nueva secuencia de colores, actualiza el estado y lo guarda en la lista `SecuenciaJuego`.
     */
    fun generarSecuencia(): List<SimonColor> {
        estadoLiveData.value = Estados.GENERANDO
        Log.d("ModelView", "Estado cambiado a GENERANDO")

        val newColor = SimonColor.entries[(0..3).random()]
        SecuenciaJuego.secuencia.add(newColor)

        estadoLiveData.value = Estados.ADIVINANDO
        Log.d("ModelView", "Nueva secuencia generada: ${SecuenciaJuego.secuencia.joinToString()}")
        return SecuenciaJuego.secuencia
    }

    /**
     * Compara la secuencia del jugador con la secuencia generada por el juego.
     * Si son iguales, la ronda se incrementa y se genera una nueva secuencia.
     */
    fun ComprobarSecuencia(): Boolean {
        val size = SecuenciaJugador.secuencia.size
        var isCorrect = true
        estadoLiveData.value = Estados.ADIVINANDO
        Log.d("ModelView", "Estado cambiado a ADIVINANDO")

        for (i in 0 until size) {
            if (SecuenciaJugador.secuencia[i] != SecuenciaJuego.secuencia[i]) {
                isCorrect = false
                estadoLiveData.value = Estados.INICIO
                Log.d("ModelView", "Secuencia incorrecta, reiniciando estado a INICIO")
                return false
            }
        }

        Log.d("ModelView", "Secuencia del jugador: ${SecuenciaJugador.secuencia.joinToString()}")
        Log.d("ModelView", "Secuencia del juego: ${SecuenciaJuego.secuencia.joinToString()}")
        Log.d("ModelView", "Secuencia correcta: $isCorrect")

        if (isCorrect) {
            if (size == SecuenciaJuego.secuencia.size) {
                Log.d("Secuencia", "Secuencia correcta, pasando a la siguiente ronda")
                aumentarRonda()
                generarSecuencia()
                clearSecuenciaJugador()
                estadoLiveData.value = Estados.ADIVINANDO
                Log.d("ModelView", "Estado cambiado a ADIVINANDO para la siguiente ronda")
                return true
            }
        } else {
            Log.d("ModelView", "Secuencia incorrecta.")
            return false
        }

        return false
    }

    /**
     * Limpia la secuencia del jugador.
     */
    fun clearSecuenciaJugador() {
        SecuenciaJugador.secuencia.clear()
        Log.d("ModelView", "Secuencia del jugador limpiada.")
    }

    /**
     * Limpia la secuencia generada por el juego.
     */
    fun clearSecuenciaJuego() {
        SecuenciaJuego.secuencia.clear()
        Log.d("ModelView", "Secuencia del juego limpiada.")
    }

    /**
     * Reinicia la ronda a cero.
     */
    fun clearRonda() {
        ronda.clear()
        Log.d("ModelView", "Ronda reiniciada.")
    }
}
