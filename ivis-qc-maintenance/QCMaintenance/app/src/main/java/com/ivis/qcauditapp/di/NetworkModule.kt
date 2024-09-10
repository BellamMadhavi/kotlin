package com.qcauditapp.di

import com.qcauditapp.retrofit.API
import com.qcauditapp.utils.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import okhttp3.logging.HttpLoggingInterceptor
import javax.net.ssl.X509TrustManager

@Module
class NetworkModule {

    @Singleton
    @Provides
    @Named("QCbaseUrl")
    fun providesRetrofitForBaseUrl1(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.QC_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    @Named("MaintenancebaseUrl")
    fun providesRetrofitForBaseUrl3(): Retrofit {
        val trustAllCertificates = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) = Unit
            override fun checkServerTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) = Unit
            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
        })

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCertificates, java.security.SecureRandom())

        val okHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCertificates[0] as X509TrustManager)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(100, TimeUnit.SECONDS) // 40-second timeout
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.Maintenance_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    @Named("QCbaseUrl")
    fun providesAPIForBaseUrl1(@Named("QCbaseUrl") retrofit: Retrofit): API {
        return retrofit.create(API::class.java)
    }
    @Singleton
    @Provides
    @Named("MaintenancebaseUrl")
    fun providesAPIForBaseUrl2(@Named("MaintenancebaseUrl") retrofit: Retrofit): API {
        return retrofit.create(API::class.java)
    }
}


