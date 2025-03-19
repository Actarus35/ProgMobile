package com.insa.mygamelist

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insa.mygamelist.api.IGDBRepository
import com.insa.mygamelist.data.Cover
import com.insa.mygamelist.data.Game
import com.insa.mygamelist.data.Genre
import com.insa.mygamelist.data.Platform
import com.insa.mygamelist.data.PlatformLogo
import kotlinx.coroutines.launch


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
            // Récupération des jeux
            val loadedGames = repository.fetchGames()
            Log.d("GameViewModel", "Nombre de jeux récupérés : ${loadedGames.size}")
            games.value = loadedGames

            //Vérification de récupération de jeux
            if (games.value.isEmpty()) {
                Log.d("Coucou", "ERREUR: Aucun jeu récupéré. Vérifie l'API.")
                return@launch
            }

            // Récupération des autres données en fonction des jeux
            val loadedCovers = repository.fetchCovers(games.value)
            val loadedGenres = repository.fetchGenres(games.value)
            val loadedPlatform = repository.fetchPlatforms(games.value)
            covers.value = loadedCovers
            genres.value = loadedGenres
            platforms.value = loadedPlatform

            // Récupération des logos de platforms en fonction des platformes
            val loadedPlatformLogo = repository.fetchPlatformLogos(platforms.value)
            platformLogos.value = loadedPlatformLogo
            //Logs de test (pour voir d'où venait mon erreur)
            Log.d("GameViewModel", "Nombre de couvertures récupérées : ${loadedCovers.size}")
            Log.d("GameViewModel", "Nombre de genres récupérés : ${loadedGenres.size}")
            Log.d("GameViewModel", "Nombre de platforms récupérés : ${loadedPlatform.size}")
            Log.d("GameViewModel", "Nombre de logos de platform récupérés : ${loadedPlatformLogo.size}")
        }
    }
    companion object
}
