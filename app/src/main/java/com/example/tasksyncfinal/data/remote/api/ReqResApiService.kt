package com.example.tasksyncfinal.data.remote.api

import com.example.tasksyncfinal.data.remote.model.LoginRequest
import com.example.tasksyncfinal.data.remote.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ReqResApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
