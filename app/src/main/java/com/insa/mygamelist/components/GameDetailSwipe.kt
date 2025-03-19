package com.insa.mygamelist.components

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.insa.mygamelist.GameDetail
import com.insa.mygamelist.data.Game
import com.insa.mygamelist.data.IGDB

// Fonction de gestion du swipe pour l'affichage des détails des jeux

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailSwipe (navController: NavController, gameDetail: GameDetail?, context: Context, filteredGames: List<Game>) {

    val game = filteredGames.find { it.id == gameDetail?.gameId }?: return
    val initialPage = filteredGames.indexOf(game)
    val pagerState = rememberPagerState(initialPage, pageCount = { filteredGames.size })
    var isFavorite by remember { mutableStateOf(IGDB.isFavorite(game)) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color(0xFFFF8C00),
                    titleContentColor = Color.Black,
                ),
                title = {
                    Row (modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically){
                        Text(game.name,
                            modifier = Modifier.weight(1f))
                        FavoriteButton(game, context, isFavorite = isFavorite, onFavoriteChange = { newFavoriteState -> isFavorite = newFavoriteState})
                    } },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = Color.Black
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        //Permet le swipe horizontal
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) {
                page -> GameDetailScreen(filteredGames[page].id, innerPadding)
        }
    }
}