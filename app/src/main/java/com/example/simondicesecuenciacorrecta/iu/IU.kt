package com.example.simondicesecuenciacorrecta.iu


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simondicesecuenciacorrecta.R

import com.example.simondicesecuenciacorrecta.datos.SimonColor
import com.example.simondicesecuenciacorrecta.modelView.ModelView
import kotlinx.coroutines.delay

/**
 * Clase IU
 *
 * Esta clase representa la interfaz de usuario del juego "Simon Dice". Implementa
 * la lógica de interacción con el usuario mediante botones que iluminan secuencias
 * de colores y reaccionan a las entradas del jugador. Utiliza Jetpack Compose para
 * diseñar la interfaz gráfica de manera declarativa.
 *
 * Funcionalidades principales:
 * - Visualización de botones de colores (rojo, azul, amarillo, verde).
 * - Gestión de la animación de la secuencia de colores.
 * - Manejo de las entradas del jugador y validación de la secuencia.
 * - Reinicio del juego y puntuación.
 */

class IU {
    /**
     * SimonGameScreen
     *
     * Componente principal de la pantalla del juego.
     * Gestiona el flujo del juego, como la animación de las secuencias de colores,
     * la interacción del jugador y el reinicio de la partida.
     *
     * Variables:
     * - `modelView`: Objeto que maneja la lógica del juego, incluyendo las secuencias y la ronda.
     * - `ronda`: Puntuación actual basada en las rondas completadas.
     * - `iluminadoIndex`: Índice del color que está iluminado en la secuencia actual.
     * - `secuenciaActual`: Lista de colores que forman la secuencia generada por el juego.
     * - `triggerAnimation`: Bandera para controlar cuándo iniciar la animación de la secuencia.
     */
    @Preview(showBackground = true)
    @Composable
    fun SimonGameScreen() {
        val modelView = remember { ModelView() }
        val ronda by remember { modelView.ronda.ronda }
        var iluminadoIndex by remember { mutableIntStateOf(-1) }
        var secuenciaActual by remember { mutableStateOf<List<SimonColor>>(emptyList()) }
        var triggerAnimation by remember { mutableStateOf(false) }
        var _activostart by remember { mutableStateOf(modelView.estadoLiveData.value!!.start_activo) }
        var _activoBoton by remember { mutableStateOf(modelView.estadoLiveData.value!!.start_activo) }

        modelView.estadoLiveData.observe(LocalLifecycleOwner.current) {
            _activostart = it.start_activo
        }
        modelView.estadoLiveData.observe(LocalLifecycleOwner.current) {
            _activoBoton = it.boton_activo
        }

        LaunchedEffect(secuenciaActual) {
            if (secuenciaActual.isNotEmpty()) {
                triggerAnimation = true
            }
        }

        LaunchedEffect(triggerAnimation) {
            if (triggerAnimation) {
                for (index in secuenciaActual.indices) {
                    iluminadoIndex = index
                    kotlinx.coroutines.delay(1000L)
                }
                iluminadoIndex = -1
                triggerAnimation = false
            }
        }
        LaunchedEffect(Unit) {

            if (triggerAnimation) {
                delay(2000L) // Retraso de 500ms
                secuenciaActual = modelView.generarSecuencia()
                triggerAnimation = true
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(236, 236, 221)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(50.dp))

                SimonButtons(secuenciaActual, iluminadoIndex = iluminadoIndex, enabled = _activoBoton && !triggerAnimation
                ) { color ->
                    modelView.SecuenciaJugador.secuencia.add(color)
                    if (modelView.ComprobarSecuencia()){
                        triggerAnimation=true
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))

                StartButton(enabled = _activostart) {
                    modelView.clearSecuenciaJuego()
                    modelView.clearSecuenciaJugador()
                    modelView.clearRonda()
                    secuenciaActual = modelView.generarSecuencia()
                    triggerAnimation = true
                }

                Spacer(modifier = Modifier.height(60.dp))

                PuntuactionButton(ronda = ronda) {
                }
            }
        }
    }

    /**
     * StartButton
     *
     * Botón de inicio del juego. Al hacer clic, resetea el estado del juego, incluyendo
     * la secuencia de colores y la puntuación, y genera una nueva secuencia.
     *
     * Parámetros:
     * - `modifier`: Modificador de diseño para personalizar el botón (opcional).
     * - `enabled`: Indica si el botón está habilitado.
     * - `onClick`: Acción a ejecutar al hacer clic en el botón.
     */

    @Composable
    fun StartButton(modifier: Modifier = Modifier, enabled: Boolean, onClick: () -> Unit) {
        Button(
            enabled = enabled,
            onClick = onClick,
            modifier = modifier.size(200.dp, 60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECECDD))
        ) {
            Image(
                painter = painterResource(id = R.drawable.bstart),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )
        }
    }
    /**
     * SimonButtons
     *
     * Botones representando los colores del juego en una cuadrícula de dos filas.
     * Iluminan secuencialmente según `iluminadoIndex` y detectan clics del usuario
     * para comparar con la secuencia generada.
     *
     * Este método utiliza el componente `SimonButtonRow` para renderizar cada fila de botones.
     *
     * Parámetros:
     * - `secuencia`: Lista de colores generada por el juego.
     * - `iluminadoIndex`: Índice del color actualmente iluminado.
     * - `onColorClick`: Acción a ejecutar cuando se hace clic en un botón de color.
     */
    @Composable
    fun SimonButtons(
        secuencia: List<SimonColor>,
        iluminadoIndex: Int,
        enabled: Boolean,
        onColorClick: (SimonColor) -> Unit
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SimonButtonRow(
                colors = listOf(SimonColor.Pink, SimonColor.Blue),
                secuencia = secuencia,
                iluminadoIndex = iluminadoIndex,
                onColorClick = onColorClick
            )
            Spacer(modifier = Modifier.height(16.dp))
            SimonButtonRow(
                colors = listOf(SimonColor.Yellow, SimonColor.Green),
                secuencia = secuencia,
                iluminadoIndex = iluminadoIndex,
                onColorClick = onColorClick
            )
        }
    }
    /**
     * SimonButtonRow
     *
     * Fila de botones que representan colores específicos del juego.
     * Cada botón puede estar iluminado según el índice proporcionado en `iluminadoIndex`.
     *
     * Parámetros:
     * - `colors`: Lista de colores a representar en la fila.
     * - `secuencia`: Lista de colores generada por el juego, utilizada para determinar el botón iluminado.
     * - `iluminadoIndex`: Índice del color actualmente iluminado en la secuencia.
     * - `onColorClick`: Acción a ejecutar cuando se hace clic en un botón de color.
     */
    @Composable
    fun SimonButtonRow(
        colors: List<SimonColor>,
        secuencia: List<SimonColor>,
        iluminadoIndex: Int,
        onColorClick: (SimonColor) -> Unit
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            colors.forEach { color ->
                ColorButton(
                    imageResId = if (iluminadoIndex != -1 && secuencia[iluminadoIndex] == color) {
                        when (color) {
                            SimonColor.Pink -> R.drawable.brosa1iluminado
                            SimonColor.Blue -> R.drawable.bazul2iluminado
                            SimonColor.Yellow -> R.drawable.bamarillo3iluminado
                            SimonColor.Green -> R.drawable.bverde4iluminado
                        }
                    } else {
                        when (color) {
                            SimonColor.Pink -> R.drawable.brosa1
                            SimonColor.Blue -> R.drawable.bazul2
                            SimonColor.Yellow -> R.drawable.bamarillo3
                            SimonColor.Green -> R.drawable.bverde4
                        }
                    },
                    onClick = { onColorClick(color) }
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
    /**
     * PuntuactionButton
     *
     * Botón que muestra la puntuación actual del jugador. La puntuación se basa en el número
     * de rondas completadas correctamente.
     *
     * Parámetros:
     * - `modifier`: Modificador de diseño para personalizar el botón (opcional).
     * - `ronda`: Ronda o puntuación actual del juego.
     * - `onClick`: Acción a ejecutar al hacer clic en el botón (opcional).
     */
    @Composable
    fun PuntuactionButton(modifier: Modifier = Modifier, ronda: Int, onClick: () -> Unit) {
        Button(
            onClick = onClick,
            modifier = modifier.size(500.dp, 100.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECECDD)),
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.bpuntuacion),
                    contentDescription = null,
                    modifier = Modifier
                        .size(300.dp)
                        .align(Alignment.Center)
                )
                Text(
                    text = "Puntuación: $ronda", fontFamily = FontFamily.Cursive, fontSize = 25.sp,
                    style = MaterialTheme.typography.bodyLarge.copy(Color.Black),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(end = 45.dp)
                )
            }
        }
    }
    /**
     * ColorButton
     *
     * Botón individual que representa un color del juego (rosa, azul, amarillo, verde).
     * Cambia su apariencia visual (imagen) según si está iluminado o no.
     *
     * Parámetros:
     * - `imageResId`: ID del recurso de imagen que representa el botón.
     * - `onClick`: Acción a ejecutar al hacer clic en el botón.
     * - `modifier`: Modificador de diseño para personalizar el botón (opcional).
     */
    @Composable
    fun ColorButton(
        imageResId: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Box(
            modifier = modifier
                .size(150.dp)
                .clickable { onClick() }
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}
