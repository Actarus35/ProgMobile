package com.insa.mygamelist.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (TokenManager.getAccessToken() == null) {
            TokenManager.loadAccessToken()
        }

        val token = TokenManager.getAccessToken()

        if (token.isNullOrEmpty()) {
            Log.e("AuthInterceptor", "Le token est toujours null, impossible de continuer.")
            throw IllegalStateException("Token d'authentification introuvable.")
        }

        val request = chain.request().newBuilder()
            .addHeader("Client-ID", TokenManager.CLIENT_ID)
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(request)
    }
}
