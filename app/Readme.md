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


## Patrón Observer y Corutinas

El patrón Observer y las corutinas se utilizan en este proyecto para manejar la reactividad y la concurrencia de manera eficiente. A continuación se explica cómo se implementan en el código:

### Patrón Observer

El patrón Observer se utiliza para observar cambios en los datos y actualizar la interfaz de usuario en consecuencia. En este proyecto, se utiliza `LiveData` para implementar el patrón Observer.

#### Implementación

1. **Definición de `LiveData` en el `ViewModel`:**
   ```kotlin
   class ModelView : ViewModel() {
       private val _estadoLiveData = MutableLiveData<Estados>()
       val estadoLiveData: LiveData<Estados> get() = _estadoLiveData

       // Otros métodos y propiedades del ViewModel
   }
   ```

2. **Observación de `LiveData` en la UI:**
   ```kotlin
   @Composable
   fun SimonGameScreen() {
       val modelView: ModelView = viewModel()
       val estado by modelView.estadoLiveData.observeAsState(Estados.INICIO)

       // Uso del estado observado en la UI
   }
   ```

### Corutinas

Las corutinas se utilizan para manejar tareas asincrónicas, como la animación de la secuencia de colores en el juego. Las corutinas permiten ejecutar estas tareas sin bloquear el hilo principal.

#### Implementación

1. **Definición de una corutina en el `ViewModel`:**
   ```kotlin
   class ModelView : ViewModel() {
       fun iniciarAnimacionSecuencia(secuencia: List<SimonColor>) {
           viewModelScope.launch {
               for (color in secuencia) {
                   // Lógica para iluminar el color
                   delay(1000L) // Espera 1 segundo entre colores
               }
           }
       }
   }
   ```

2. **Llamada a la corutina desde la UI:**
   ```kotlin
   @Composable
   fun SimonGameScreen() {
       val modelView: ModelView = viewModel()
       var secuenciaActual by remember { mutableStateOf<List<SimonColor>>(emptyList()) }
       var triggerAnimation by remember { mutableStateOf(false) }

       LaunchedEffect(triggerAnimation) {
           if (triggerAnimation) {
               modelView.iniciarAnimacionSecuencia(secuenciaActual)
               triggerAnimation = false
           }
       }

       // Lógica de la UI
   }
   ```

Estas implementaciones permiten que la interfaz de usuario reaccione a los cambios en los datos y maneje tareas asincrónicas de manera eficiente, mejorando la experiencia del usuario.
Así dependiendo de cada estado el botón start o los botones de colores están blouqeados o no dependiendo de lo que nosotros tenemos estipulado en nuestra clase datos. 



## Cómo Jugar

1. Presiona el botón de inicio para comenzar el juego.
2. Observa la secuencia de colores que se ilumina.
3. Repite la secuencia presionando los botones de colores en el mismo orden.
4. La secuencia se hará más larga con cada ronda.
5. El juego termina si te equivocas en la secuencia.
