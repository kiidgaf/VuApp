package com.example.vuapp.api


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Path
import com.example.vuapp.DashboardResponse



//  Login request/response
data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val keypass: String
)

//  DELETE this duplicated version of DashboardEntity — it's already defined in com.example.vuapp

//  Retrofit interface
interface ApiService {

    @POST("sydney/auth") // or footscray/auth or ort/auth
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("dashboard/{keypass}")
    suspend fun getDashboardData(
        @Path("keypass") keypass: String
    ): Response<DashboardResponse>
    //  Uses the correct DashboardEntity from com.example.vuapp
}

