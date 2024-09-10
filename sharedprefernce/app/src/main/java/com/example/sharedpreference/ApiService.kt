package com.example.sharedpreference

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("login/login.php")
    fun login(
        @Query("mobile") mobile: String,
        @Query("pin") pin: String
    ): Call<LoginResponse>
}
