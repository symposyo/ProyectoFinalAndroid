package com.example.tasksyncfinal.data.remote.model

import com.google.gson.annotations.SerializedName

data class TaskResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("todo")
    val title: String,
    @SerializedName("completed")
    val isCompleted: Boolean,
    @SerializedName("userId")
    val userId: Int
)
