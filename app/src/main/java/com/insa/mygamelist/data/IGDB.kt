package com.insa.mygamelist.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.insa.mygamelist.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object IGDB {

    lateinit var covers: List<Cover>
    lateinit var games: List<Game>
    lateinit var genres: List<Genre>
    lateinit var platform_logos: List<PlatformLogo>
    lateinit var platforms: List<Platform>
    private var favorites: MutableList<Long> = mutableListOf()

    fun load(context: Context) {
        val gson = Gson()

        covers = gson.fromJson(
            context.resources.openRawResource(R.raw.covers).bufferedReader(),
            object : TypeToken<List<Cover>>() {}.type
        )

        games = gson.fromJson(
            context.resources.openRawResource(R.raw.games).bufferedReader(),
            object : TypeToken<List<Game>>() {}.type
        )

        genres = gson.fromJson(
            context.resources.openRawResource(R.raw.genres).bufferedReader(),
            object : TypeToken<List<Genre>>() {}.type
        )

        platform_logos = gson.fromJson(
            context.resources.openRawResource(R.raw.platform_logos).bufferedReader(),
            object : TypeToken<List<PlatformLogo>>() {}.type
        )

        platforms = gson.fromJson(
            context.resources.openRawResource(R.raw.platforms).bufferedReader(),
            object : TypeToken<List<Platform>>() {}.type
        )

        favorites = FavoriteManager.loadFavorites(context).toMutableList()
    }

    fun toggleFavorite(context: Context, game: Game){
        if (favorites.contains(game.id)){
            favorites.remove(game.id)
        } else {
            favorites.add(game.id)
        }
        FavoriteManager.saveFavorites(context, favorites)
    }

    fun isFavorite(game: Game): Boolean {
        return favorites.contains(game.id)
    }

}

@Serializable
data class Cover(val id: Long, val url: String)
@Serializable
data class Game(val id: Long, val cover: Long, @SerialName ("first_release_date") val firstReleasedDate: Long, val genres: List<Long>, val name: String, val platforms: List<Int>, val summary: String, @SerialName ("total_rating") val totalRating: Double)
@Serializable
data class Genre(val id: Long, val name: String)
@Serializable
data class PlatformLogo(val id: Long, val url: String)
@Serializable
data class Platform(val id: Int, val name: String, @SerialName ("platform_logo") val platformLogo: Long)
