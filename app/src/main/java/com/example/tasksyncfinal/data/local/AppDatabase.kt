package com.example.tasksyncfinal.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * CONFIGURACIÓN DE LA BASE DE DATOS (ROOM):
 * Esta clase es el punto de acceso principal a la base de datos persistente.
 */
@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Exponemos el DAO para que el resto de la app pueda usarlo.
    abstract fun taskDao(): TaskDao

    companion object {
        // @Volatile asegura que el valor de INSTANCE siempre esté actualizado para todos los hilos.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * PATRÓN SINGLETON:
         * Usamos esta función para asegurar que solo exista UNA instancia de la base de datos
         * en toda la aplicación, ahorrando recursos de memoria.
         */
        fun getDatabase(context: Context): AppDatabase {
            // Si ya existe una instancia, la devolvemos.
            return INSTANCE ?: synchronized(this) {
                // Si no existe, creamos la base de datos usando Room.databaseBuilder.
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "task_database" // Nombre del archivo SQLite en el dispositivo.
                )
                // Estrategia sencilla: si cambiamos la versión de la DB, borramos lo viejo y creamos lo nuevo.
                .fallbackToDestructiveMigration() 
                .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
