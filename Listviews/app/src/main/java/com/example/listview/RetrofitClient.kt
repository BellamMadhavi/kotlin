package com.example.listview
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitClient {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://dev2-v2.iviscloud.net/api/") // Replace with your actual base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
