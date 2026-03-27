package com.example.tasksyncfinal.data.remote.api

import com.example.tasksyncfinal.data.remote.model.TaskResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface JsonPlaceholderApiService {
    @GET("todos")
    suspend fun getTasks(
        @Query("limit") limit: Int = 10,
        @Query("skip") skip: Int = 0
    ): Response<TaskResponseList>
}

data class TaskResponseList(
    val todos: List<TaskResponse>,
    val total: Int,
    val skip: Int,
    val limit: Int
)
