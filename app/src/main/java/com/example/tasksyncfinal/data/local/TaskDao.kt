package com.example.tasksyncfinal.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * INTERFAZ DAO (Data Access Object):
 * Aquí definimos las consultas SQL que Room ejecutará. 
 * Es el "traductor" entre nuestro código Kotlin y la base de datos SQLite.
 */
@Dao
interface TaskDao {

    /**
     * Obtener todas las tareas de la tabla 'tasks' ordenadas por ID descendente.
     * El tipo 'List<TaskEntity>' indica que Room nos devolverá una lista de objetos listos para usar.
     */
    @Query("SELECT * FROM tasks ORDER BY localId DESC")
    suspend fun getAllTasks(): List<TaskEntity>

    /**
     * Insertar una lista de tareas (usado en la sincronización con la API).
     * El parámetro 'onConflict = OnConflictStrategy.REPLACE' asegura que si una tarea
     * ya existe (mismo ID), se actualice con la nueva información.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    /**
     * Insertar una sola tarea (usado para la creación local).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    /**
     * Limpiar la tabla de tareas (útil para hacer un "reset" de la app).
     */
    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()
}
