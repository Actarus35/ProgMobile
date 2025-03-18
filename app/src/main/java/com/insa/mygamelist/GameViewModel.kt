package com.insa.mygamelist

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.insa.mygamelist.api.IGDBRepository
import com.insa.mygamelist.data.*


class GameViewModel : ViewModel() {

    var games = mutableStateOf<List<Game>>(emptyList())
    var covers = mutableStateOf<List<Cover>>(emptyList())
    var genres = mutableStateOf<List<Genre>>(emptyList())
    var platforms = mutableStateOf<List<Platform>>(emptyList())
    var platformLogos = mutableStateOf<List<PlatformLogo>>(emptyList())
    var repository = IGDBRepository()

    // Fonction pour charger les données avec gestion des dépendances
    fun loadData() {
        viewModelScope.launch {
            // Récupérer d'abord les jeux
            val loadedGames = repository.fetchGames()
            Log.d("GameViewModel", "Nombre de jeux récupérés : ${loadedGames.size}")
            games.value = loadedGames

            if (games.value.isEmpty()) {
                Log.d("Coucou", "ERREUR: Aucun jeu récupéré. Vérifie l'API.")
                return@launch
            }

            // Puis récupérer les autres données en fonction des jeux
            val loadedCovers = repository.fetchCovers(games.value)  // Si tu as besoin d'utiliser les jeux pour récupérer les covers
            val loadedGenres = repository.fetchGenres(games.value)  // Si tu veux lier genres aux jeux
            val loadedPlatform = repository.fetchPlatforms(games.value)  // Si tu as besoin de lier les platforms aux jeux
            covers.value = loadedCovers
            genres.value = loadedGenres
            platforms.value = loadedPlatform

            val loadedPlatformLogo = repository.fetchPlatformLogos(platforms.value)  // Si tu veux lier les logos aux platforms
            platformLogos.value = loadedPlatformLogo
            Log.d("GameViewModel", "Nombre de couvertures récupérées : ${loadedCovers.size}")
            Log.d("GameViewModel", "Nombre de genres récupérés : ${loadedGenres.size}")
            Log.d("GameViewModel", "Nombre de platforms récupérés : ${loadedPlatform.size}")
            Log.d("GameViewModel", "Nombre de logos de platform récupérés : ${loadedPlatformLogo.size}")
        }
    }
    companion object
}
