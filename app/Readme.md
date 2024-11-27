# Simon Dice 

Simon Dice es un juego de memoria donde los jugadores deben repetir una secuencia de colores generada por el juego. La secuencia se hace más larga con cada ronda, desafiando la memoria del jugador.

## Patron MVVC

El proyecto sigue el patrón de arquitectura Modelo-Vista-VistaModelo (MVVM). Las clases de la aplicación se dividen en tres categorías:

### Model
EL modelo o los datos. Las clases que representan los datos están almacenados en el paquete datos que son: 

- Estados (enum class)
- Ronda
- Secuencia Juego
- Secuencia Jugador
- SimonColor (enum class)

Estas clases se usan para llamar a los datos guardads en la aplicación

### View

La vista o la interfaz de usuario. Las clases que definen la interfaz de usuario están almacenadas en el paquete IU. Estas clases son:

- IU

En esta clase tenemos todas nuestras Composables que se encargan de mostrar la interfaz de usuario de la aplicación.

- SimonGameScreen
- StartButton
- SimonButtons
- PuntuactionButton
- ColorButton
- SimonButtonRow

Todas estas clases hacen con que nuestro programa sea visible y trabajan con la ModelView que es la que tiene nuestras funciones

### ViewModel

El ViewModel o la lógica de negocio. Las clases que contienen la lógica de negocio están almacenadas en el paquete lógica. Estas clases son:

- ModelView

Contiene las siguientes funciones:

- `iniciarJuego()`: Inicia un nuevo juego.
- `comprobarSecuencia()`: Comprueba si la secuencia del jugador coincide con la secuencia del juego.
- `generarSecuencia()`: Genera una nueva secuencia de colores.
- `siguienteRonda()`: Avanza a la siguiente ronda del juego.
-  `clearRonda()`: Reinicia la ronda del juego.
- `clearSecuencia()`: Reinicia la secuencia del juego.
- `clearSecuenciaJugador()`: Reinicia la secuencia del jugador.

Estas funciones se encargan de manejar la lógica del juego y de comunicarse con la vista y con los datos. Es decir que la vista no se comunica directamente con los datos, sino que lo hace a través del ViewModel.



## Cómo Jugar

1. Presiona el botón de inicio para comenzar el juego.
2. Observa la secuencia de colores que se ilumina.
3. Repite la secuencia presionando los botones de colores en el mismo orden.
4. La secuencia se hará más larga con cada ronda.
5. El juego termina si te equivocas en la secuencia.
