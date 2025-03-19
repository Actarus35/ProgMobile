@file:Suppress("UnusedImport")

package com.insa.mygamelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.navigation.compose.rememberNavController
import com.insa.mygamelist.api.TokenManager
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.ui.theme.MyGamesListTheme
import com.insa.mygamelist.components.*
import kotlinx.serialization.Serializable

@Serializable
object GameList

@Serializable
data class GameDetail(val gameId: Long)

@Suppress("unused")
class MainActivity : ComponentActivity() {

    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TokenManager.loadAccessToken()

        //IGDB.loadWithApi(this, gameViewModel)

        IGDB.load(this)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val darkTheme = isSystemInDarkTheme()
            MyGamesListTheme(darkTheme = darkTheme) {
                AppNavigation(navController, this)
            }
        }
    }
}

