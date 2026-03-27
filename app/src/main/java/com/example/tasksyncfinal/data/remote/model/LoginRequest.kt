package com.example.tasksyncfinal.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * MODELO DE PETICIÓN DE LOGIN:
 * Definimos los datos que el servidor espera recibir. 
 */
data class LoginRequest(
    // Usamos @SerializedName para que Retrofit sepa que en el JSON debe llamarse "username"
    // aunque en nuestro código Kotlin usemos la variable 'username'.
    @SerializedName("username")
    val username: String,

    val password: String
)
