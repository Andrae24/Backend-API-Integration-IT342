package com.example.backendapiintegration.network

import com.example.backendapiintegration.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    // Supabase Signup Route
    @POST("auth/v1/signup")
    fun register(@Body request: RegisterRequest): Call<AuthResponse>

    // Supabase Login Route
    @POST("auth/v1/token?grant_type=password")
    fun login(@Body request: LoginRequest): Call<AuthResponse>

    // Supabase Get User Profile
    @GET("auth/v1/user")
    fun getProfile(): Call<ProfileResponse>

    // Supabase Update User (Email/Password)
    @PUT("auth/v1/user")
    fun updateProfile(@Body request: Any): Call<BaseResponse>
}