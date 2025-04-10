package com.example.vuapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuapp.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _items = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val items: StateFlow<List<Map<String, Any>>> = _items

    fun loadDashboardData(keypass: String) {
        viewModelScope.launch {
            try {
                val dashboardItems = repository.getDashboardItems(keypass)
                _items.value = dashboardItems
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
