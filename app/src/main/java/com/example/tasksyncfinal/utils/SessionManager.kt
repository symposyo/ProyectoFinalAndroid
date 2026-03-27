package com.example.tasksyncfinal.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * GESTOR DE SESIÓN (SharedPreferences):
 * Esta clase envuelve la persistencia simple de Android para manejar el Token.
 * Se usa para que el usuario no tenga que loguearse cada vez que abre la app.
 */
class SessionManager(context: Context) {

    // Archivo de preferencias privado. Solo nuestra app puede leerlo.
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "auth_token"
    }

    /**
     * Guarda el token recibido de la API.
     */
    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    /**
     * Recupera el token guardado. Si no existe, devuelve null.
     */
    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    /**
     * Borra los datos de sesión (Logout).
     */
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
