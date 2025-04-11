package com.example.vuapp

import com.example.vuapp.viewmodel.LoginViewModel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.vuapp.api.LoginResponse
import com.example.vuapp.repository.LoginRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import okhttp3.ResponseBody
import retrofit2.Response
import okhttp3.MediaType.Companion.toMediaType // Correct import for toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody

class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginRepository: LoginRepository

    // Rule to allow LiveData operations to be executed synchronously
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        loginRepository = mockk() // Mock the LoginRepository
        loginViewModel = LoginViewModel(loginRepository) // Initialize ViewModel with mocked repository
    }

    @Test
    fun `login should return keypass when login is successful`() = runTest {
        // Arrange: Create a mock response for successful login
        val mockLoginResponse = LoginResponse(keypass = "mock_keypass")
        val mockResponse = Response.success(mockLoginResponse) // Mock Retrofit response

        // Mock the repository's login function to return the mock response
        coEvery { loginRepository.login("user", "password") } returns mockResponse

        // Act: Call the login function in the ViewModel
        loginViewModel.login("user", "password")

        // Collect the result from the ViewModel's StateFlow
        loginViewModel.loginResult.collect { result ->
            // Assert: Verify the result is successful and contains the expected keypass
            assertTrue(result?.isSuccess == true)
            assertEquals("mock_keypass", result?.getOrNull())
        }
    }

    @Test
    fun `login should fail when login is unsuccessful`() = runTest {
        // Arrange: Create a mock unsuccessful response
        val mockErrorResponse: Response<LoginResponse> = Response.error(
            401,
            "Unauthorized".toResponseBody("application/json".toMediaType()) // Correct usage of toMediaType
        )

        // Mock the repository's login function to return the unsuccessful response
        coEvery { loginRepository.login("user", "password") } returns mockErrorResponse

        // Act: Call the login function in the ViewModel
        loginViewModel.login("user", "password")

        // Collect the result from the ViewModel's StateFlow
        loginViewModel.loginResult.collect { result ->
            // Assert: Verify the result is a failure
            assertTrue(result?.isFailure == true)
        }
    }

    @Test
    fun `login should handle exceptions correctly`() = runTest {
        // Arrange: Mock an exception thrown by the repository
        coEvery { loginRepository.login("user", "password") } throws Exception("Network error")

        // Act: Call the login function in the ViewModel
        loginViewModel.login("user", "password")

        // Collect the result from the ViewModel's StateFlow
        loginViewModel.loginResult.collect { result ->
            // Assert: Verify the result is a failure with the expected exception
            assertTrue(result?.isFailure == true)
            assertEquals("Network error", result?.exceptionOrNull()?.message)
        }
    }
}

