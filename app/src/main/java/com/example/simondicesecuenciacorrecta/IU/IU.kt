package com.example.simondicesecuenciacorrecta.IU

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simondicesecuenciacorrecta.R
import com.example.simondicesecuenciacorrecta.datos.Ronda
import com.example.simondicesecuenciacorrecta.datos.SecuenciaJuego
import com.example.simondicesecuenciacorrecta.datos.SecuenciaJugador
import com.example.simondicesecuenciacorrecta.datos.SimonColor
import com.example.simondicesecuenciacorrecta.modelView.ModelView

class IU {

    @Preview(showBackground = true)
    @Preview(showBackground = true)
    @Composable
    fun SimonGameScreen() {
        val modelView = remember { ModelView() }
        val ronda by remember { modelView.ronda.ronda }
        val secuenciaJuego by remember { mutableStateOf(modelView.SecuenciaJuego.secuencia) }
        var iluminadoIndex by remember { mutableIntStateOf(-1) }

        LaunchedEffect(secuenciaJuego) {
            secuenciaJuego.forEachIndexed { index, _ ->
                iluminadoIndex = index
                kotlinx.coroutines.delay(500L)
            }
            iluminadoIndex = -1
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

                SimonButtons(secuenciaJuego, iluminadoIndex) { color ->
                    modelView.SecuenciaJugador.secuencia.add(color)
                    modelView.ComprobarSecuencia()
                }

                Spacer(modifier = Modifier.height(50.dp))

                StartButton(enabled = true) {
                    modelView.clearSecuenciaJuego()
                    modelView.clearSecuenciaJugador()
                    modelView.generarSecuencia()
                }

                Spacer(modifier = Modifier.height(60.dp))

                PuntuactionButton(ronda = ronda) {
                }
            }
        }
    }


    @Composable
    fun SimonButtons(
        secuencia: List<SimonColor>,
        iluminadoIndex: Int,
        onColorClick: (SimonColor) -> Unit
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ColorButton(
                    imageResId = if (iluminadoIndex == 0) R.drawable.brosa1iluminado else R.drawable.brosa1,
                    onClick = { onColorClick(SimonColor.Pink) }
                )
                Spacer(modifier = Modifier.width(16.dp))
                ColorButton(
                    imageResId = if (iluminadoIndex == 1) R.drawable.bazul2iluminado else R.drawable.bazul2,
                    onClick = { onColorClick(SimonColor.Blue) }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ColorButton(
                    imageResId = if (iluminadoIndex == 2) R.drawable.bamarillo3iluminado else R.drawable.bamarillo3,
                    onClick = { onColorClick(SimonColor.Yellow) }
                )
                Spacer(modifier = Modifier.width(16.dp))
                ColorButton(
                    imageResId = if (iluminadoIndex == 3) R.drawable.bverde4iluminado else R.drawable.bverde4,
                    onClick = { onColorClick(SimonColor.Green) }
                )
            }
        }
    }




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
    @Composable
    fun ColorButton(
        imageResId: Int, // ID de la imagen del botón
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


    @Composable
    fun AnimarSecuencia(modelView: ModelView) {
        var iluminadoIndex by remember { mutableIntStateOf(-1) }

        LaunchedEffect(modelView.SecuenciaJuego.secuencia) {
            modelView.SecuenciaJuego.secuencia.forEachIndexed { index, _ ->
                iluminadoIndex = index
                kotlinx.coroutines.delay(500L) // Retardo de 500ms entre colores
            }
            iluminadoIndex = -1 // Apagar iluminación al final
        }
    }
}

