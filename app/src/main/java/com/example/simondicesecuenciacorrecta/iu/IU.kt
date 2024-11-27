package com.example.simondicesecuenciacorrecta.iu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.example.simondicesecuenciacorrecta.datos.Estados
import com.example.simondicesecuenciacorrecta.datos.SimonColor
import com.example.simondicesecuenciacorrecta.modelView.ModelView
import kotlinx.coroutines.delay

/**
 * Clase IU
 *
 * Esta clase representa la interfaz de usuario del juego "Simón Dice". Implementa
 * la lógica de interacción del usuario a través de botones que iluminan secuencias de colores y
 * reaccionan a las entradas del jugador. Utiliza Jetpack Compose para diseñar la interfaz gráfica
 * de manera declarativa.
 *
 * Funcionalidades principales:
 * - Mostrar botones de colores (rojo, azul, amarillo, verde).
 * - Gestionar la animación de la secuencia de colores.
 * - Manejar las entradas del jugador y validar la secuencia.
 * - Restablecer el juego y puntuar.
 */
class IU {

    /**
     * SimonGameScreen
     *
     * Componente principal de la pantalla del juego.
     * Gestiona el flujo del juego, como la animación de la secuencia de colores,
     * la interacción del jugador y el restablecimiento del juego.
     *
     * Variables:
     * - `modelView`: Objeto que maneja la lógica del juego, incluidas las secuencias y rondas.
     * - `ronda`: Puntuación actual basada en las rondas completadas.
     * - `iluminadoIndex`: Índice del color que está iluminado actualmente en la secuencia.
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
                    delay(1000L)
                }
                iluminadoIndex = -1
                triggerAnimation = false
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

                // SimonButtons con el estado de habilitación de los botones
                SimonButtons(
                    secuencia = secuenciaActual,
                    iluminadoIndex = iluminadoIndex,
                    enabled = _activoBoton,  // Pasar el estado del botón
                    onColorClick = { color ->
                        modelView.SecuenciaJugador.secuencia.add(color)
                        if (modelView.ComprobarSecuencia()) {
                            triggerAnimation = true
                        }
                    }
                )

                Spacer(modifier = Modifier.height(50.dp))

                // StartButton con el estado de habilitación
                StartButton(enabled = _activostart) {
                    modelView.clearSecuenciaJuego()
                    modelView.clearSecuenciaJugador()
                    modelView.clearRonda()
                    secuenciaActual = modelView.generarSecuencia()
                    triggerAnimation = true
                }

                Spacer(modifier = Modifier.height(60.dp))

                // Botón de puntuación
                PuntuactionButton(ronda = ronda) {}
            }
        }
    }

    /**
     * StartButton
     *
     * Botón para iniciar el juego. Al hacer clic, restablece el estado del juego, incluidas
     * la secuencia de colores y la puntuación, y genera una nueva secuencia.
     *
     * Parámetros:
     * - `modifier`: Modificador de diseño para personalizar el botón (opcional).
     * - `enabled`: Indica si el botón está habilitado.
     * - `onClick`: Acción a ejecutar cuando se hace clic en el botón.
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
     * Botones que representan los colores del juego en una cuadrícula de dos filas.
     * Se iluminan secuencialmente según `iluminadoIndex` y detectan los clics del usuario
     * para compararlos con la secuencia generada.
     *
     * Este método usa el componente `SimonButtonRow` para renderizar cada fila de botones.
     *
     * Parámetros:
     * - `secuencia`: Lista de colores generados por el juego.
     * - `iluminadoIndex`: Índice del color que está iluminado actualmente.
     * - `enabled`: Propiedad para habilitar/deshabilitar los botones.
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
                enabled = enabled,
                onColorClick = onColorClick
            )
            Spacer(modifier = Modifier.height(16.dp))
            SimonButtonRow(
                colors = listOf(SimonColor.Yellow, SimonColor.Green),
                secuencia = secuencia,
                iluminadoIndex = iluminadoIndex,
                enabled = enabled,
                onColorClick = onColorClick
            )
        }
    }

    /**
     * SimonButtonRow
     *
     * Fila de botones que representan colores específicos del juego.
     * Cada botón puede iluminarse según el `iluminadoIndex` proporcionado.
     *
     * Parámetros:
     * - `colors`: Lista de colores a representar en la fila.
     * - `secuencia`: Lista de colores generados por el juego, usada para determinar el botón iluminado.
     * - `iluminadoIndex`: Índice del color que está iluminado actualmente en la secuencia.
     * - `enabled`: Propiedad para habilitar/deshabilitar los botones.
     * - `onColorClick`: Acción a ejecutar cuando se hace clic en un botón de color.
     */
    @Composable
    fun SimonButtonRow(
        colors: List<SimonColor>,
        secuencia: List<SimonColor>,
        iluminadoIndex: Int,
        enabled: Boolean,
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
                    enabled = enabled,
                    onClick = { onColorClick(color) }
                )
            }
        }
    }

    /**
     * ColorButton
     *
     * Botón de color que representa un color específico del juego.
     *
     * Parámetros:
     * - `imageResId`: Identificador del recurso de imagen que se mostrará en el botón.
     * - `enabled`: Indica si el botón está habilitado.
     * - `onClick`: Acción a ejecutar cuando se hace clic en el botón.
     */
    @Composable
    fun ColorButton(imageResId: Int, enabled: Boolean, onClick: () -> Unit) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .padding(8.dp)
                .clickable(enabled = enabled, onClick = onClick)
        ) {
            Image(painter = painterResource(id = imageResId), contentDescription = null)
        }
    }

    /**
     * PuntuactionButton
     *
     * Botón que muestra la puntuación actual del jugador.
     *
     * Parámetros:
     * - `ronda`: Puntuación del jugador.
     * - `onClick`: Acción a ejecutar cuando se hace clic en el botón.
     */
    @Composable
    fun PuntuactionButton(ronda: Int, onClick: () -> Unit) {
        Button(onClick = onClick) {
            Text(text = "Puntuación: $ronda", fontFamily = FontFamily.Default, fontSize = 16.sp)
        }
    }
}
