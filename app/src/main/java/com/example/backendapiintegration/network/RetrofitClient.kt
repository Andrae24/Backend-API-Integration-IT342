package com.example.backendapiintegration.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // 1. Paste your Supabase Project URL here (Make sure it ends with a slash!)
    private const val BASE_URL = "https://hpjigvbovrderlsaxigj.supabase.co/"

    // 2. Paste your Supabase anon/public key here
    private const val SUPABASE_KEY = "sb_publishable_IE2al3GIGMPlWbcNHx5bkg_OYTSHbhA"

    fun getInstance(context: Context): ApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = Interceptor { chain ->
            val sharedPreferences = context.getSharedPreferences("APP", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("TOKEN", null)

            val requestBuilder = chain.request().newBuilder()

            // ALWAYS add the Supabase API Key
            requestBuilder.addHeader("apikey", SUPABASE_KEY)

            // Add the Bearer token if the user is logged in, otherwise use the anon key
            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            } else {
                requestBuilder.addHeader("Authorization", "Bearer $SUPABASE_KEY")
            }

            chain.proceed(requestBuilder.build())
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}