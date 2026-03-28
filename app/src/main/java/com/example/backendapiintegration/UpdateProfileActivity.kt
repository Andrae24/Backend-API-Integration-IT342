package com.example.backendapiintegration

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.backendapiintegration.databinding.ActivityUpdateProfileBinding
import com.example.backendapiintegration.models.BaseResponse
import com.example.backendapiintegration.models.UpdateProfileRequest
import com.example.backendapiintegration.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSaveProfile.setOnClickListener {
            updateUserData()
        }
    }

    private fun updateUserData() {
        val newName = binding.etUpdateName.text.toString().trim()
        val newEmail = binding.etUpdateEmail.text.toString().trim()

        if (newName.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        binding.updateProgressBar.visibility = View.VISIBLE
        binding.btnSaveProfile.isEnabled = false

        val request = UpdateProfileRequest(newName, newEmail)

        RetrofitClient.getInstance(this).updateProfile(request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                binding.updateProgressBar.visibility = View.GONE
                binding.btnSaveProfile.isEnabled = true

                if (response.isSuccessful) {
                    Toast.makeText(this@UpdateProfileActivity, "Profile Updated!", Toast.LENGTH_SHORT).show()
                    finish() // Return to Profile screen
                } else {
                    Toast.makeText(this@UpdateProfileActivity, "Update Failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                binding.updateProgressBar.visibility = View.GONE
                binding.btnSaveProfile.isEnabled = true
                Toast.makeText(this@UpdateProfileActivity, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}