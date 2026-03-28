package com.example.backendapiintegration

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.backendapiintegration.databinding.ActivityChangePasswordBinding
import com.example.backendapiintegration.models.BaseResponse
import com.example.backendapiintegration.models.ChangePasswordRequest
import com.example.backendapiintegration.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnUpdatePassword.setOnClickListener {
            performPasswordUpdate()
        }
    }

    private fun performPasswordUpdate() {
        val newPassword = binding.etNewPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        if (newPassword.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPassword != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        binding.passwordProgressBar.visibility = View.VISIBLE
        binding.btnUpdatePassword.isEnabled = false

        // In Supabase, we send the new password in the body
        val request = ChangePasswordRequest(newPassword, confirmPassword)

        RetrofitClient.getInstance(this).updateProfile(request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                binding.passwordProgressBar.visibility = View.GONE
                binding.btnUpdatePassword.isEnabled = true

                if (response.isSuccessful) {
                    Toast.makeText(this@ChangePasswordActivity, "Password Updated!", Toast.LENGTH_SHORT).show()
                    finish() // Go back to Profile
                } else {
                    Toast.makeText(this@ChangePasswordActivity, "Failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                binding.passwordProgressBar.visibility = View.GONE
                binding.btnUpdatePassword.isEnabled = true
                Toast.makeText(this@ChangePasswordActivity, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}