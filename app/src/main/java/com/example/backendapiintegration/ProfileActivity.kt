package com.example.backendapiintegration

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.backendapiintegration.databinding.ActivityProfileBinding
import com.example.backendapiintegration.models.ProfileResponse
import com.example.backendapiintegration.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadProfile()

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(this, UpdateProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnChangePassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadProfile() {
        binding.profileProgressBar.visibility = View.VISIBLE

        RetrofitClient.getInstance(this).getProfile().enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                binding.profileProgressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    binding.tvProfileEmail.text = response.body()?.email
                    binding.tvProfileId.text = response.body()?.id
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                binding.profileProgressBar.visibility = View.GONE
                Toast.makeText(this@ProfileActivity, "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
        })
    }
}