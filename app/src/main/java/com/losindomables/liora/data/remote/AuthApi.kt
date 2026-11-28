package com.losindomables.liora.data.remote

import com.losindomables.liora.data.remote.dto.AuthResponse
import com.losindomables.liora.data.remote.dto.LoginRequest
import com.losindomables.liora.data.remote.dto.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Endpoints de autenticación del backend (liora-api-backend).
 * Se devuelve [Response] para poder leer el código HTTP (400, 401, 409)
 * sin que Retrofit lance una excepción.
 */
interface AuthApi {

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
}
