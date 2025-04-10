package com.example.vuapp

data class DashboardResponse(
    val entities: List<Map<String, Any>>,
    val entityTotal: Int
)
