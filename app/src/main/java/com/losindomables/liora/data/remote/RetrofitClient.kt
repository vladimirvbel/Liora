package com.losindomables.liora.data.remote

import com.losindomables.liora.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton que construye Retrofit apuntando al backend desplegado en Render.
 */
object RetrofitClient {

    private const val BASE_URL = "https://api.liora-app.me/"

    // Timeouts largos: Render (plan gratuito) puede tardar en "despertar" (cold start).
    private const val TIMEOUT_SECONDS = 60L

    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)

        // El logging con BODY expone contraseñas y tokens: solo en debug.
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
        }
        builder.build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
}
