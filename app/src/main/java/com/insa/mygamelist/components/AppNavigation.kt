package com.insa.mygamelist.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.insa.mygamelist.GameDetail
import com.insa.mygamelist.GameList
import com.insa.mygamelist.data.Game
import com.insa.mygamelist.data.IGDB

//Fonction de navigation
@Composable
fun AppNavigation(navController: NavHostController, context: Context) {

    var filteredGames by remember { mutableStateOf<List<Game>>(IGDB.games) }

    NavHost(navController, startDestination = GameList) {
        // Premier composable vers la liste de jeux
        composable<GameList> {
            GameListScreen(navController, context, filteredGames) { newFilteredGames ->
                filteredGames = newFilteredGames
            }
        }
        // Deuxième composable vers les détails d'un jeu permettant le swipe
        composable <GameDetail> { backStackEntry ->
            val gameDetail = backStackEntry.arguments?.let { bundle ->
                GameDetail(gameId = bundle.getLong("gameId"))
            }
            GameDetailSwipe(navController, gameDetail, context, filteredGames)
        }
    }
}