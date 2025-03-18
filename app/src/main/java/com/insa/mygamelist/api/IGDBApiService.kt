package com.insa.mygamelist.api

import com.insa.mygamelist.data.*
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface IGDBApiService {

    @Headers("Accept: application/json")
    @POST("games")
    suspend fun getGames(@Body body: RequestBody): Response<List<Game>>

    @Headers("Accept: application/json")
    @POST("covers")
    suspend fun getCovers(@Body body: RequestBody): Response<List<Cover>>

    @Headers("Accept: application/json")
    @POST("genres")
    suspend fun getGenres(@Body body: RequestBody): Response<List<Genre>>

    @Headers("Accept: application/json")
    @POST("platform_logos")
    suspend fun getPlatformLogos(@Body body: RequestBody): Response<List<PlatformLogo>>

    @Headers("Accept: application/json")
    @POST("platforms")
    suspend fun getPlatforms(@Body body: RequestBody): Response<List<Platform>>
}
