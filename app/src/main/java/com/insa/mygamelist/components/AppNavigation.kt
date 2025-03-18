package com.insa.mygamelist.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.insa.mygamelist.GameDetail
import com.insa.mygamelist.GameList

@Composable
fun AppNavigation(navController: NavHostController, context: Context) {

    NavHost(navController, startDestination = GameList) {
        composable<GameList> {
            GameListScreen(navController, context)
        }
        composable <GameDetail> { backStackEntry ->
            val gameDetail = backStackEntry.arguments?.let { bundle ->
                GameDetail(gameId = bundle.getLong("gameId"))
            }
            GameDetailScreen(navController, gameDetail, context)
        }
    }
}