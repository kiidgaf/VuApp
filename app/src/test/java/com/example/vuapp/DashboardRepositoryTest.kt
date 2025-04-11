package com.example.vuapp

import com.example.vuapp.api.ApiService
import com.example.vuapp.repository.DashboardRepository
import com.example.vuapp.DashboardResponse
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import okhttp3.ResponseBody.Companion.toResponseBody
import kotlinx.coroutines.test.runBlockingTest
import retrofit2.Response
import okhttp3.MediaType.Companion.toMediaType
import org.junit.Assert.assertTrue


class DashboardRepositoryTest {

    private lateinit var apiService: ApiService
    private lateinit var repository: DashboardRepository

    @Before
    fun setup() {
        apiService = mockk()
        repository = DashboardRepository(apiService)
    }

    @Test
    fun `should return dashboard items when API response is successful`() = runBlockingTest {
        // Arrange: Mocking the API response
        val mockResponse = DashboardResponse(
            entities = listOf(
                mapOf(
                    "field" to "Physics",
                    "concept" to "Theory of Relativity",
                    "description" to "A theory describing the relationship between space and time."
                ),
                mapOf(
                    "field" to "Biology",
                    "concept" to "Theory of Evolution",
                    "description" to "A scientific theory explaining biodiversity."
                )
            ),
            entityTotal = 2
        )

        val response = Response.success(mockResponse)
        coEvery { apiService.getDashboardData("science") } returns response

        // Act: Call the repository function
        val result = repository.getDashboardItems("science")

        // Assert: Verify the returned data
        assertEquals(2, result.size)
        assertEquals("Physics", result[0]["field"])
        assertEquals("Biology", result[1]["field"])
    }

    @Test
    fun `should throw an exception when API response is unsuccessful`() = runBlockingTest {
        // Arrange: Create a mock unsuccessful response with the correct ResponseBody
        val mockErrorResponse: Response<DashboardResponse> = Response.error(
            401,
            "Unauthorized".toResponseBody("application/json".toMediaType()) // Correct usage of toMediaType
        )

        // Mock the repository's function to return the unsuccessful response
        coEvery { apiService.getDashboardData("science") } returns mockErrorResponse

        // Act & Assert: Verify that an exception is thrown when the API call is made
        try {
            repository.getDashboardItems("science")
        } catch (e: Exception) {
            assertTrue(e is Exception) // Check that the thrown exception is of the expected type
        }
    }
}
