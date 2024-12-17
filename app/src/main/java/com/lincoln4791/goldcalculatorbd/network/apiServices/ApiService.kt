package com.lincoln4791.goldcalculatorbd.network.apiServices;

import com.lincoln4791.goldcalculatorbd.ui.auth.login.models.LoginRequest
import com.lincoln4791.goldcalculatorbd.ui.auth.login.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}
