package com.insa.mygamelist.api

import android.util.Log
import com.insa.mygamelist.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("UselessCallOnCollection")
class IGDBRepository() {
    private val api: IGDBApiService

    init {
        val client = okhttp3.OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.igdb.com/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        api = retrofit.create(IGDBApiService::class.java)
    }

    suspend fun fetchGames(): List<Game> {
        return withContext(Dispatchers.IO) {
            val body = "fields name, cover, first_release_date, genres, platforms, summary, total_rating; limit 10;"
                .toRequestBody()
            val response = api.getGames(body)
            if (response.isSuccessful) {
                val games = response.body() ?: emptyList()
                Log.d("IGDBRepository", "Jeux récupérés : ${games.size}")
                games
            }
            else {
                Log.e("IGDBRepository", "Erreur API fetchGames: ${response.errorBody()?.string()}")
                emptyList()
            }
        }
    }

    suspend fun fetchCovers(games: List<Game>): List<Cover> {
        val coverIds = games.mapNotNull { it.cover }
        if (coverIds.isEmpty()) return emptyList()
        return withContext(Dispatchers.IO) {
            val body = ("fields url; where id = (" + coverIds.joinToString(",") + "); limit 500;")
                .toRequestBody()
            val response = api.getCovers(body)
            if (response.isSuccessful) response.body() ?: emptyList()
            else emptyList()
        }
    }

    suspend fun fetchGenres(games: List<Game>): List<Genre> {
        val genreIds = games.mapNotNull { it.genres }
        if (genreIds.isEmpty()) return emptyList()
        val genreIdsString = genreIds.flatten().joinToString(",")
        Log.d("DEBUG_GENRE", genreIdsString)
        return withContext(Dispatchers.IO) {
            val body = "fields name; where id = ($genreIdsString); limit 500;"
                .toRequestBody()
            val response = api.getGenres(body)
            if (response.isSuccessful) response.body() ?: emptyList()
            else emptyList()
        }
    }


    suspend fun fetchPlatforms(games: List<Game>): List<Platform> {
        val platformIdsString = games.mapNotNull { it.platforms }
            .flatten()
            .distinct()
            .joinToString(",")
        Log.d("DEBUG_PLATFORM", "Plateformes récupérées : $platformIdsString")
        return withContext(Dispatchers.IO) {
            val body = "fields name, platform_logo; where id = ($platformIdsString); limit 500;"
                .toRequestBody()
            Log.d("DEBUG_API_REQUEST", "Requête envoyée : fields name, platform_logo; where id = ($platformIdsString); limit 500;")
            val response = api.getPlatforms(body)
            Log.d("DEBUG_API_PLATFORMS", "Réponse API plateformes : ${response.body()}")
            if (response.isSuccessful) response.body() ?: emptyList()
            else emptyList()
        }
    }

    suspend fun fetchPlatformLogos(platforms: List<Platform>): List<PlatformLogo> {
        val platformLogosIds = platforms.mapNotNull { it.platform_logo }
        val platformLogosIdsString = platformLogosIds.joinToString(",")
        return withContext(Dispatchers.IO) {
            val body = "fields url; where id = ($platformLogosIdsString); limit 500;"
                .toRequestBody()
            val response = api.getPlatformLogos(body)
            Log.d("DEBUG_PLATFORM_LOGO", "Platform logos récupérés : $platformLogosIdsString")
            if (response.isSuccessful) response.body() ?: emptyList()
            else emptyList()
        }
    }

}
