package com.example.tasksyncfinal.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tasksyncfinal.data.remote.api.RetrofitProviders
import com.example.tasksyncfinal.data.remote.model.LoginRequest
import com.example.tasksyncfinal.databinding.ActivityLoginBinding
import com.example.tasksyncfinal.ui.tasks.TaskListActivity
import com.example.tasksyncfinal.utils.SessionManager
import kotlinx.coroutines.launch

/**
 * PANTALLA DE LOGIN: El punto de entrada para usuarios no autenticados.
 * Aquí aplicamos: ViewBinding, Corrutinas, Retrofit y Persistencia simple (SharedPreferences).
 */
class LoginActivity : AppCompatActivity() {

    // ViewBinding: Para acceder a las vistas del XML (activity_login.xml) de forma segura.
    private lateinit var binding: ActivityLoginBinding
    
    // SessionManager: Clase de utilidad para guardar el Token en SharedPreferences.
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Inicializamos ViewBinding.
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Inicializamos el gestor de sesión.
        sessionManager = SessionManager(this)

        // 3. Configuramos el listener del botón de login.
        binding.btnLogin.setOnClickListener {
            performLogin()
        }
    }

    /**
     * Lógica principal del Login.
     */
    private fun performLogin() {
        // Obtenemos los valores de los campos de texto (previamente configurados con valores por defecto).
        val userInput = binding.etEmail.text.toString().trim()
        val passwordInput = binding.etPassword.text.toString().trim()

        // Validaciones básicas de seguridad: no enviar campos vacíos a la API.
        if (userInput.isBlank() || passwordInput.isBlank()) {
            Toast.makeText(this, "Completa usuario y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        // UI Feedback: Informamos al usuario y bloqueamos el botón para evitar múltiples clics.
        binding.tvStatus.text = "Estado: enviando credenciales al servidor..."
        binding.btnLogin.isEnabled = false

        // Lanzamos una Corrutina en el lifecycleScope: Se cancelará automáticamente si la Activity muere.
        lifecycleScope.launch {
            try {
                // LLAMADA A LA API (Retrofit):
                // Usamos DummyJSON. Pasamos el usuario y contraseña al constructor de LoginRequest.
                val response = RetrofitProviders.reqResApi.login(LoginRequest(userInput, passwordInput))

                if (response.isSuccessful) {
                    // Si el servidor responde 200 OK, extraemos el token.
                    val token = response.body()?.token

                    if (!token.isNullOrBlank()) {
                        // GUARDAR TOKEN: Crucial para mantener la sesión activa tras cerrar la app.
                        sessionManager.saveToken(token)
                        
                        binding.tvStatus.text = "Estado: login correcto. Token guardado."
                        
                        // NAVEGACIÓN: Vamos a la pantalla principal de tareas.
                        startActivity(Intent(this@LoginActivity, TaskListActivity::class.java))
                        
                        // Cerramos esta Activity para que el usuario no pueda volver atrás al login con el botón 'Back'.
                        finish()
                    } else {
                        binding.tvStatus.text = "Estado: login correcto pero sin token."
                    }
                } else {
                    // Manejo de errores de API (400, 401, etc.)
                    binding.tvStatus.text = "Estado: error ${response.code()} en login."
                    Toast.makeText(
                        this@LoginActivity,
                        "Credenciales inválidas o error del servicio",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                // Manejo de errores de RED (sin internet, timeout, etc.)
                binding.tvStatus.text = "Estado: ${e.message}"
                Toast.makeText(
                    this@LoginActivity,
                    "Fallo de red: ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                // Siempre rehabilitamos el botón, ocurra error o no.
                binding.btnLogin.isEnabled = true
            }
        }
    }
}
