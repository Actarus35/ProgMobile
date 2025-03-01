package com.insa.mygamelist.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private val Context.dataStore by preferencesDataStore(name = "favorites_store")

object FavoriteManager {
    private val FAVORITES_KEY = stringPreferencesKey("favorites")

    fun saveFavorites(context: Context, favorites: List<Long>) {
        val json = Gson().toJson(favorites)
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[FAVORITES_KEY] = json
            }
        }
    }

    fun loadFavorites(context: Context): List<Long> {
        return runBlocking {
            val json = context.dataStore.data.map { preferences ->
                preferences[FAVORITES_KEY] ?: "[]"
            }.first()

            Gson().fromJson(json, object : TypeToken<List<Long>>() {}.type) ?: emptyList()
        }
    }
}
