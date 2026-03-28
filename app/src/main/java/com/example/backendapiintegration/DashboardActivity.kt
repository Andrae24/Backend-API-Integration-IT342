package com.example.backendapiintegration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.backendapiintegration.databinding.ActivityDashboardBinding
import com.example.backendapiintegration.models.ProfileResponse
import com.example.backendapiintegration.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Fetch user data immediately
        fetchUserProfile()

        // 2. Logout logic
        binding.btnLogout.setOnClickListener {
            // Clear the token from SharedPreferences
            getSharedPreferences("APP", Context.MODE_PRIVATE).edit().clear().apply()

            // Go back to Login
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        binding.btnViewProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }


    }

    private fun fetchUserProfile() {
        binding.progressBar.visibility = View.VISIBLE

        RetrofitClient.getInstance(this).getProfile().enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val user = response.body()
                    binding.tvUserEmail.text = "Email: ${user?.email}"
                    binding.tvUserId.text = "ID: ${user?.id}"
                } else {
                    Toast.makeText(this@DashboardActivity, "Session Expired", Toast.LENGTH_SHORT).show()
                    // If 401, they need to log in again
                    finish()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@DashboardActivity, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}