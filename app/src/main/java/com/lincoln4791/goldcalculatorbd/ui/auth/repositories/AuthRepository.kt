package com.lincoln4791.goldcalculatorbd.ui.auth.repositories

import com.lincoln4791.goldcalculatorbd.network.apiServices.ApiService
import com.lincoln4791.goldcalculatorbd.ui.auth.login.models.LoginRequest
import com.lincoln4791.goldcalculatorbd.ui.auth.login.models.LoginResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse> {
        return try {
            val response = apiService.login(loginRequest)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
