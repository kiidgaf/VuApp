package com.example.vuapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuapp.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    private val _loginResult = MutableStateFlow<Result<String>?>(null)
    val loginResult: StateFlow<Result<String>?> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(username, password)
                if (response.isSuccessful && response.body() != null) {
                    val keypass = response.body()!!.keypass
                    Log.d("LoginViewModel", "Login successful, keypass: $keypass")
                    _loginResult.value = Result.success(keypass)
                } else {
                    Log.e("LoginViewModel", "Login failed: ${response.code()} ${response.message()}")
                    _loginResult.value = Result.failure(Exception("Login failed"))
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login exception: ${e.message}")
                _loginResult.value = Result.failure(e)
            }
        }
    }
}


