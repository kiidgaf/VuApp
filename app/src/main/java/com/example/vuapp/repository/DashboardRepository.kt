package com.example.vuapp.repository

import com.example.vuapp.api.ApiService

import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getDashboardItems(keypass: String): List<Map<String, Any>> {
        val response = apiService.getDashboardData(keypass)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.entities
        } else {
            throw Exception("Error fetching dashboard")
        }
    }

}
