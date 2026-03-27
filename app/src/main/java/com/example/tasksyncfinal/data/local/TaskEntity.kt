package com.example.tasksyncfinal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * CAPA DE DATOS LOCAL (ROOM):
 * Esta clase define la estructura de la tabla en la base de datos SQLite.
 */
@Entity(tableName = "tasks") // Definimos el nombre de la tabla.
data class TaskEntity(
    // Clave primaria autoincremental para el control interno de la App.
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,
    
    // ID que viene de la API (opcional, ya que las tareas locales no tienen uno al inicio).
    val remoteId: Int?,
    
    val userId: Int?,
    
    // El contenido de la tarea.
    val title: String,
    
    // Estado de la tarea.
    val completed: Boolean,
    
    // Metadato para saber si la tarea vino de la "API" o fue creada "LOCAL"mente.
    val source: String
)
