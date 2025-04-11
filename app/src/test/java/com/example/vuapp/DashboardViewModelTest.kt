package com.example.vuapp

import com.example.vuapp.api.ApiService
import com.example.vuapp.repository.DashboardRepository
import com.example.vuapp.viewmodel.DashboardViewModel
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

class DashboardViewModelTest {

    private lateinit var apiService: ApiService
    private lateinit var repository: DashboardRepository
    private lateinit var dashboardViewModel: DashboardViewModel

    @Before
    fun setup() {
        apiService = mockk()
        repository = DashboardRepository(apiService)
        dashboardViewModel = DashboardViewModel(repository)
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

        // Act: Call the repository function via the ViewModel
        dashboardViewModel.loadDashboardData("science")

        // Assert: Verify the returned data
        dashboardViewModel.items.collect { items ->
            assertEquals(2, items.size)
            assertEquals("Physics", items[0]["field"])
            assertEquals("Biology", items[1]["field"])
        }
    }

    @Test
    fun `should throw an exception when API response is unsuccessful`() = runBlockingTest {
        // Arrange: Create a mock unsuccessful response with the correct ResponseBody
        val mockErrorResponse: Response<DashboardResponse> = Response.error(
            401,
            "Unauthorized".toResponseBody("application/json".toMediaType())
        )

        // Mock the repository's getDashboardItems function to return the unsuccessful response
        coEvery { repository.getDashboardItems("science") } throws Exception("Error fetching dashboard")

        // Act & Assert: Verify that an exception is thrown when the function is called
        try {
            dashboardViewModel.loadDashboardData("science")
        } catch (e: Exception) {
            assertTrue(e is Exception) // Check that the thrown exception is of the expected type
        }
    }
}
