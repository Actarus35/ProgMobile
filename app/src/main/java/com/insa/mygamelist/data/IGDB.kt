package com.insa.mygamelist.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.insa.mygamelist.GameViewModel
import com.insa.mygamelist.R

object IGDB {

    var games: List<Game> = emptyList()
    var covers: List<Cover> = emptyList()
    var genres: List<Genre> = emptyList()
    var platforms: List<Platform> = emptyList()
    var platformLogos: List<PlatformLogo> = emptyList()
    private var favorites: MutableList<Long> = mutableListOf()

    //Récupération des données avec l'API
    @Suppress("unused")
    fun loadWithApi(context: Context, gameViewModel: GameViewModel) {
        gameViewModel.loadData()
        FavoriteManager.saveFavorites(context, favorites)
    }

    //Récupération des données sans l'API
    @Suppress("unused")
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

        platformLogos = gson.fromJson(
            context.resources.openRawResource(R.raw.platform_logos).bufferedReader(),
            object : TypeToken<List<PlatformLogo>>() {}.type
        )

        platforms = gson.fromJson(
            context.resources.openRawResource(R.raw.platforms).bufferedReader(),
            object : TypeToken<List<Platform>>() {}.type
        )

        Log.d("DEBUG PLATFORM", platforms.toString())

        favorites = FavoriteManager.loadFavorites(context).toMutableList()
    }

    //Gestion des favoris lorsqu'on clique sur l'étoile et sauvegarde dans la base de données
    fun toggleFavorite(context: Context, game: Game){
        if (favorites.contains(game.id)){
            favorites.remove(game.id)
        } else {
            favorites.add(game.id)
        }
        FavoriteManager.saveFavorites(context, favorites)
    }

    //Fonction de vérification si un jeu est un favori
    fun isFavorite(game: Game): Boolean {
        return favorites.contains(game.id)
    }

}