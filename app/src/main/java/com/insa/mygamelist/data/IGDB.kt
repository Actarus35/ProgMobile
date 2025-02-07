package com.insa.mygamelist.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.insa.mygamelist.R
import kotlinx.serialization.Serializable

object IGDB {

    lateinit var covers: List<Cover>
    lateinit var games: List<Game>
    lateinit var genres: List<Genre>
    lateinit var platform_logos: List<PlatformLogo>
    lateinit var platforms: List<Platform>

    fun load(context: Context) {

        val coversFromJsonCover: List<Cover> = Gson().fromJson(
            context.resources.openRawResource(R.raw.covers).bufferedReader(),
            object : TypeToken<List<Cover>>() {}.type
        )
        covers = coversFromJsonCover
        val coversFromJsonGame: List<Game> = Gson().fromJson(
            context.resources.openRawResource(R.raw.games).bufferedReader(),
            object : TypeToken<List<Game>>() {}.type
        )
        games = coversFromJsonGame
        val coversFromJsonGenre: List<Genre> = Gson().fromJson(
            context.resources.openRawResource(R.raw.genres).bufferedReader(),
            object : TypeToken<List<Genre>>() {}.type
        )
        genres = coversFromJsonGenre
        val coversFromJsonPlatformLogo: List<PlatformLogo> = Gson().fromJson(
            context.resources.openRawResource(R.raw.platform_logos).bufferedReader(),
            object : TypeToken<List<PlatformLogo>>() {}.type
        )
        platform_logos = coversFromJsonPlatformLogo
        val coversFromJsonPlatform: List<Platform> = Gson().fromJson(
            context.resources.openRawResource(R.raw.platforms).bufferedReader(),
            object : TypeToken<List<Platform>>() {}.type
        )
        platforms = coversFromJsonPlatform
    }
}

@Serializable
data class Cover(val id: Long, val url: String)
@Serializable
data class Game(val id: Long, val cover: Long, val firstReleaseDate: Long, val genres: List<Long>, val name: String, val platforms: List<Int>, val summary: String, val totalRating: Double)
@Serializable
data class Genre(val id: Long, val name: String)
@Serializable
data class PlatformLogo(val id: Long, val url: String)
@Serializable
data class Platform(val id: Int, val name: String, val platformLogo: Long)
