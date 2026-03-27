package com.example.tasksyncfinal.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasksyncfinal.data.local.TaskEntity
import com.example.tasksyncfinal.databinding.ItemTaskBinding

/**
 * ADAPTADOR DEL RECYCLERVIEW:
 * Es el encargado de tomar una lista de datos (TaskEntity) y "dibujar" cada elemento
 * en una fila de la pantalla usando un diseño XML (item_task.xml).
 */
class TaskAdapter(
    // Lista interna que contiene los datos a mostrar.
    private val items: MutableList<TaskEntity> = mutableListOf()
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    /**
     * VIEWHOLDER: Es un contenedor que "sostiene" las vistas de una fila.
     * Sirve para que Android no tenga que buscar los elementos (findViewById)
     * cada vez que el usuario hace scroll, mejorando mucho el rendimiento.
     */
    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Función Bind: Mapea los datos de un objeto TaskEntity a las vistas del XML.
         */
        fun bind(task: TaskEntity) {
            binding.tvTaskTitle.text = task.title
            
            // Mostramos información extra para que en clase se vea de dónde viene el dato.
            binding.tvTaskInfo.text = "Origen: ${task.source} | userId: ${task.userId ?: "-"} | remoteId: ${task.remoteId ?: "-"}"
            
            // Marcamos el checkbox según el estado de la tarea.
            binding.cbCompleted.isChecked = task.completed
        }
    }

    /**
     * Se llama cuando el RecyclerView necesita crear una "fila" nueva (un ViewHolder).
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // Inflamos el diseño de la fila (item_task.xml).
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    /**
     * Se llama para asociar datos específicos a una fila que ya fue creada.
     */
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(items[position])
    }

    /**
     * Le dice al RecyclerView cuántos elementos hay en total.
     */
    override fun getItemCount(): Int = items.size

    /**
     * Función de utilidad para actualizar la lista completa y refrescar la pantalla.
     */
    fun updateData(newItems: List<TaskEntity>) {
        items.clear()
        items.addAll(newItems)
        // Notificamos al adaptador que los datos cambiaron para que se redibuje.
        notifyDataSetChanged()
    }
}
