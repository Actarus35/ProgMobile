package com.insa.mygamelist.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

object TokenManager {
    internal const val CLIENT_ID = "YourClientId"
    private const val CLIENT_SECRET = "YourClientSecret"
    private const val TOKEN_URL = "https://id.twitch.tv/oauth2/token"
    private var accessToken: String? = null

    fun getAccessToken(): String? {
        if (accessToken == null) {
            Log.e("TokenManager", "Token is null. Attempting to load a new one.")
            loadAccessToken()  // Si pas de token, tente de le charger
        }
        return accessToken
    }

    fun loadAccessToken() {
        if (accessToken == null) {
            accessToken = fetchNewToken()
        }
    }

    private fun fetchNewToken(): String? {
        return try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("$TOKEN_URL?client_id=$CLIENT_ID&client_secret=$CLIENT_SECRET&grant_type=client_credentials")
                .post(okhttp3.FormBody.Builder().build())
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (!response.isSuccessful || responseBody == null) {
                Log.e("TokenManager", "Échec de la récupération du token")
                null
            }else {
                val json = JSONObject(responseBody)
                val newToken = json.getString("access_token")
                Log.d("TokenManager", "Token récupéré : $newToken")
                accessToken = newToken
                newToken
            }
        } catch (e: Exception) {
            Log.e("TokenManager", "Erreur lors de la récupération du token : ${e.message}")
            null
        }
    }
}
