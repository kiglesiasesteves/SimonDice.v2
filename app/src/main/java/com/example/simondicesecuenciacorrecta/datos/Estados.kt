package com.example.simondicesecuenciacorrecta.datos

enum class Estados (val start_activo: Boolean, val boton_activo: Boolean){
    INICIO(start_activo = true, boton_activo = false),
    GENERANDO(start_activo = false, boton_activo = false),
    ADIVINANDO(start_activo = false, boton_activo = true)
}