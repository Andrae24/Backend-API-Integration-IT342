package com.example.backendapiintegration.models

import com.google.gson.annotations.SerializedName

// --- Requests ---
data class RegisterRequest(
    val email: String,
    val password: String,
    val data: Map<String, String>
)
data class LoginRequest(val email: String, val password: String)
data class UpdateProfileRequest(val name: String, val email: String)
data class ChangePasswordRequest(val currentPassword: String, val newPassword: String)

// --- Responses ---
data class AuthResponse(
    @SerializedName("access_token")
    val token: String,
    val msg: String? = null
)

data class ProfileResponse(val id: String, val email: String)
data class BaseResponse(val message: String)

// Add your other models here...
class ApiModels {

}