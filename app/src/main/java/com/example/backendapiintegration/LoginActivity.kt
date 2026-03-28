package com.example.backendapiintegration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.backendapiintegration.databinding.ActivityLoginBinding
import com.example.backendapiintegration.models.LoginRequest
import com.example.backendapiintegration.models.AuthResponse
import com.example.backendapiintegration.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    // ViewBinding automatically connects our Kotlin to our XML layout
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // When the user clicks the button, trigger the API call
        binding.btnLogin.setOnClickListener {
            performLogin()
        }
        binding.tvGoToRegister.setOnClickListener {
            val intent = android.content.Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun performLogin() {
        // Grab the text the user actually typed in
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // 1. UI Updates: Show loading, disable button so they don't double-click
        binding.progressBar.visibility = View.VISIBLE
        binding.btnLogin.isEnabled = false

        val request = LoginRequest(email, password)

        RetrofitClient.getInstance(this).login(request).enqueue(object : Callback<AuthResponse> {

            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                // Reset UI
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true

                when {
                    response.isSuccessful -> {
                        // 200 OK - Save Token
                        val token = response.body()?.token
                        getSharedPreferences("APP", Context.MODE_PRIVATE)
                            .edit()
                            .putString("TOKEN", token)
                            .apply()

                        Toast.makeText(this@LoginActivity, "Login Success!", Toast.LENGTH_SHORT).show()

                        // TODO: Navigate to Dashboard Activity here
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish() // Close Login so they can't go "Back" to it

                    }
                    response.code() == 400 || response.code() == 401 -> {
                        Toast.makeText(this@LoginActivity, "Invalid email or password", Toast.LENGTH_LONG).show()
                    }
                    response.code() == 500 -> {
                        Toast.makeText(this@LoginActivity, "Server Error. Please try again later.", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@LoginActivity, "An unexpected error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                // Reset UI
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true

                // No internet or timeout
                Toast.makeText(this@LoginActivity, "Network failure: Check your internet connection", Toast.LENGTH_LONG).show()
            }
        })
    }
}