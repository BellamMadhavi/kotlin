package com.example.listview

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json", "Customer-Name: MANAP-C")
    @POST("/clogin")  // Replace with the actual endpoint
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>
}