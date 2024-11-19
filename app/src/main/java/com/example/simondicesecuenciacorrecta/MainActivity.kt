package com.example.simondicesecuenciacorrecta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.simondicesecuenciacorrecta.iu.IU
import com.example.simondicesecuenciacorrecta.ui.theme.SimonDiceSecuenciaCorrectaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimonDiceSecuenciaCorrectaTheme {
                val iu = IU()
                iu.SimonGameScreen()            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimonDiceSecuenciaCorrectaTheme {
        val iu = IU()
        iu.SimonGameScreen()    }
}
