package com.example.tasksyncfinal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tasksyncfinal.databinding.ActivityMainBinding
import com.example.tasksyncfinal.ui.login.LoginActivity
import com.example.tasksyncfinal.ui.tasks.TaskListActivity
import com.example.tasksyncfinal.utils.SessionManager

/**
 * ACTIVIDAD LANZADORA (Router):
 * Esta es la primera actividad que se ejecuta al abrir la app.
 * Su único objetivo es decidir si enviamos al usuario al Login o a la Lista de Tareas.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Inicializamos el gestor de sesión para verificar el Token.
        sessionManager = SessionManager(this)

        // 2. Lógica de Enrutamiento:
        // Si hay un token guardado, el usuario ya se logueó anteriormente.
        binding.tvMainStatus.text = if (sessionManager.getToken().isNullOrBlank()) {
            "No hay sesión activa. Redirigiendo a Login..."
        } else {
            "Sesión encontrada. Redirigiendo a lista de tareas..."
        }

        // 3. Pequeño delay de 900ms para que se vea el "estado" (efecto Splash Screen).
        binding.root.postDelayed({
            if (sessionManager.getToken().isNullOrBlank()) {
                // REDIRIGIR A LOGIN: No hay token.
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                // REDIRIGIR A TAREAS: Ya tenemos sesión.
                startActivity(Intent(this, TaskListActivity::class.java))
            }
            // Importante: Cerramos esta actividad para que no quede en la pila de navegación.
            finish()
        }, 900)
    }
}
