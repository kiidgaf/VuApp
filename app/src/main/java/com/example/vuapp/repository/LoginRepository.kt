package com.example.vuapp.repository

import com.example.vuapp.api.ApiService
import com.example.vuapp.api.LoginRequest
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun login(username: String, password: String) =
        api.login(LoginRequest(username, password))
}
