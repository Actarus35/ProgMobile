package com.insa.mygamelist.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private val Context.dataStore by preferencesDataStore(name = "favorites_store")

//Gestion des favoris et communication avec la base de données

object FavoriteManager {
    private val FAVORITES_KEY = stringPreferencesKey("favorites")

    //Enregistrement des favoris dans la base de données
    fun saveFavorites(context: Context, favorites: List<Long>) {
        val json = Gson().toJson(favorites)
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[FAVORITES_KEY] = json
            }
        }
    }

    //Chargement des favoris de la base de données
    fun loadFavorites(context: Context): List<Long> {
        return runBlocking {
            val json = context.dataStore.data.map { preferences ->
                preferences[FAVORITES_KEY] ?: "[]"
            }.first()

            Gson().fromJson(json, object : TypeToken<List<Long>>() {}.type) ?: emptyList()
        }
    }
}
