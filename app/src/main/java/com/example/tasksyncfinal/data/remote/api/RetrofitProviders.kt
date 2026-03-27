package com.example.tasksyncfinal.data.remote.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * PROVEEDORES DE RETROFIT (Capa de Red):
 * Centralizamos la configuración de red aquí. Usamos el patrón 'Object' para que sea un 
 * Singleton accesible desde cualquier parte de la app.
 */
object RetrofitProviders {

    // 1. Interceptor de Logs: Permite ver en el Logcat (pestaña inferior de Android Studio)
    // todas las peticiones que salen y entran (Cuerpo, Headers, URL).
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // 2. Cliente OkHttp: Es el "motor" que realmente hace las peticiones HTTP.
    // Le añadimos el interceptor de logs para depurar fallos en clase.
    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    /**
     * API DE AUTENTICACIÓN (DummyJSON):
     * Se usa para validar usuario y contraseña y obtener un Token.
     * Usamos 'by lazy' para que no se cree hasta que alguien lo llame por primera vez.
     */
    val reqResApi: ReqResApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://dummyjson.com/") // Servidor de destino.
            .client(client) // Motor de red.
            .addConverterFactory(GsonConverterFactory.create()) // Traduce JSON a Objetos Kotlin automáticamente.
            .build()
            .create(ReqResApiService::class.java) // Genera la implementación de la interfaz.
    }

    /**
     * API DE TAREAS:
     * En este caso usamos el mismo proveedor (DummyJSON), pero lo separamos por si en el futuro
     * el profesor quiere enseñar cómo conectar con una segunda API distinta.
     */
    val jsonPlaceholderApi: JsonPlaceholderApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JsonPlaceholderApiService::class.java)
    }
}
