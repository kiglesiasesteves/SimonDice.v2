package com.example.simondicesecuenciacorrecta

import android.os.Bundle
import android.os.DeadObjectException
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
        try {
            setContent {
                SimonDiceSecuenciaCorrectaTheme {
                    val iu = IU()
                    iu.SimonGameScreen()
                }
            }
        } catch (e: DeadObjectException) {
            // Handle the exception gracefully
            e.printStackTrace()
            // Optionally, restart the activity or show an error message to the user
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimonDiceSecuenciaCorrectaTheme {
        val iu = IU()
        iu.SimonGameScreen()
    }
}