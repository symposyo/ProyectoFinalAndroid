package com.example.tasksyncfinal.ui.tasks

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasksyncfinal.data.local.AppDatabase
import com.example.tasksyncfinal.data.local.TaskEntity
import com.example.tasksyncfinal.data.remote.api.RetrofitProviders
import com.example.tasksyncfinal.databinding.ActivityTaskListBinding
import com.example.tasksyncfinal.ui.login.LoginActivity
import com.example.tasksyncfinal.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * PANTALLA PRINCIPAL: Gestión de Tareas (Sincronización API + Room Local).
 * En esta pantalla enseñamos: Listas (RecyclerView), Room Database y Sincronización.
 */
class TaskListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskListBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var database: AppDatabase
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Inicialización de componentes.
        sessionManager = SessionManager(this)
        database = AppDatabase.getDatabase(this) // Room Singleton.
        adapter = TaskAdapter()

        // 2. Control de Acceso: Si alguien llega aquí sin token (p.ej. vía deep link), lo echamos.
        if (sessionManager.getToken().isNullOrBlank()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setupUi()
        
        // 3. Carga Inicial: Mostramos lo que ya tenemos en el teléfono (offline-first).
        loadLocalTasks()
    }

    private fun setupUi() {
        // Configuración del RecyclerView: Cómo se dibujará la lista.
        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        binding.rvTasks.adapter = adapter

        binding.tvWelcome.text = "Bienvenido al proyecto final"
        // Mostramos solo un pedazo del token por seguridad/estética.
        binding.tvToken.text = "Token: ${sessionManager.getToken()?.take(15)}..."

        // BOTÓN AGREGAR LOCAL: Demuestra inserción directa en base de datos.
        binding.btnAddLocal.setOnClickListener {
            addLocalTask()
        }

        // BOTÓN SINCRONIZAR: El "corazón" del proyecto (Red -> Local -> UI).
        binding.btnSync.setOnClickListener {
            syncTasksFromApi()
        }

        // BOTÓN LOGOUT: Limpiar preferencias y volver al inicio.
        binding.btnLogout.setOnClickListener {
            sessionManager.clearSession()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    /**
     * Carga de datos desde la Base de Datos Local (ROOM).
     */
    private fun loadLocalTasks() {
        lifecycleScope.launch {
            // CONSULTA EN HILO SECUNDARIO (IO): Room no permite consultas en el hilo principal.
            val tasks = withContext(Dispatchers.IO) {
                database.taskDao().getAllTasks()
            }
            // VOLVEMOS AL HILO PRINCIPAL: Actualizamos la UI con los datos obtenidos.
            adapter.updateData(tasks)
            binding.tvStatusTasks.text = "Estado: ${tasks.size} tareas cargadas desde Room"
        }
    }

    /**
     * Insertar una tarea de prueba manualmente en Room.
     */
    private fun addLocalTask() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                database.taskDao().insertTask(
                    TaskEntity(
                        remoteId = null,
                        userId = null,
                        title = "Tarea local creada en clase",
                        completed = false,
                        source = "LOCAL"
                    )
                )
            }
            binding.tvStatusTasks.text = "Estado: tarea local agregada"
            loadLocalTasks() // Refrescamos la lista.
        }
    }

    /**
     * SINCRONIZACIÓN: Descarga datos de internet y los guarda en la base de datos local.
     */
    private fun syncTasksFromApi() {
        lifecycleScope.launch {
            binding.tvStatusTasks.text = "Estado: sincronizando tareas desde la API..."
            try {
                // LLAMADA RETROFIT: Obtenemos datos de DummyJSON.
                val response = RetrofitProviders.jsonPlaceholderApi.getTasks(limit = 15)

                if (response.isSuccessful) {
                    // Si la API responde bien, procesamos la lista de 'todos'.
                    val remoteTasks = response.body()?.todos ?: emptyList()

                    // CONVERSIÓN (Mapping): Transformamos objetos de API a entidades de ROOM.
                    val entities = remoteTasks.map {
                        TaskEntity(
                            remoteId = it.id,
                            userId = it.userId,
                            title = it.title,
                            completed = it.isCompleted,
                            source = "API"
                        )
                    }

                    // PERSISTENCIA: Guardamos todo en Room. 
                    // Si la tarea ya existe (por ID), se reemplaza (OnConflictStrategy.REPLACE).
                    withContext(Dispatchers.IO) {
                        database.taskDao().insertTasks(entities)
                    }

                    binding.tvStatusTasks.text = "Estado: ${entities.size} tareas sincronizadas y guardadas en Room"
                    
                    // IMPORTANTE: Tras guardar en Room, volvemos a cargar la lista desde Room.
                    // Así mantenemos a Room como la "Única Fuente de Verdad" (Single Source of Truth).
                    loadLocalTasks()
                } else {
                    binding.tvStatusTasks.text = "Estado: error ${response.code()} al consultar tareas"
                }
            } catch (e: Exception) {
                binding.tvStatusTasks.text = "Estado: ${e.message}"
                Toast.makeText(this@TaskListActivity, "Fallo al sincronizar: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
