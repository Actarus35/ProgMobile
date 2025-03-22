package com.insa.mygamelist.components

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.insa.mygamelist.GameDetail
import com.insa.mygamelist.data.Game
import com.insa.mygamelist.data.IGDB

// Fenêtre avec la liste des jeux
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GameListScreen(
    navController: NavController,
    context: Context,
    filteredGames: List<Game>,
    onFilterChange: (List<Game>) -> Unit
) {

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isSearching by rememberSaveable { mutableStateOf(false) }
    var isFavoriteList by rememberSaveable {mutableStateOf(false)}

    //Gestion des favoris avec la base de données
    val favoriteStates = remember {
        mutableStateMapOf<Long, Boolean>().apply {
            putAll(IGDB.games.associate { it.id to IGDB.isFavorite(it) })
        }
    }

    // Barre principale avec outil de recherche
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color(0xFFFF8C00),
                    titleContentColor = Color.Black,
                ),
                title = {
                    if (isSearching) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                label = {
                                    Text(
                                        text = "Recherche un jeu ...",
                                        color = Color.Black
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    //Tout en noir quel que soit le thème
                                    unfocusedBorderColor = Color.Black,
                                    focusedBorderColor = Color.Black,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black
                                )
                            )
                            IconButton(onClick = { isSearching = false; searchQuery = "" }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Fermer la recherche",
                                    tint = Color.Black
                                )
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                        Text("My Games List", modifier = Modifier.weight((1f)))
                        IconButton(onClick = { isFavoriteList = !isFavoriteList }) {
                            Icon(
                                if (isFavoriteList) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = "Tous mes favoris",
                                tint = if (isFavoriteList) Color.Yellow else Color.LightGray
                            )
                        }
                            }
                    }
                },
                actions = {
                    if (!isSearching) {
                        IconButton(onClick = { isSearching = true }) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Rechercher",
                                tint = Color.Black
                            )
                        }
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            //Permet l'affichage dynamique des filtres
            LaunchedEffect(searchQuery, isFavoriteList) {
                onFilterChange(
                    if (searchQuery.isBlank()) {
                        if(isFavoriteList) {
                            IGDB.games.filter { game -> favoriteStates[game.id] == true }
                        } else {
                            IGDB.games
                        }
                    } else {
                        IGDB.games.filter { game ->
                            (game.name.contains(searchQuery, ignoreCase = true) ||
                                    game.genres.any { genreId ->
                                        IGDB.genres.find { it.id == genreId }?.name?.contains(searchQuery, ignoreCase = true) == true
                                    } ||
                                    game.platforms.any { platformID ->
                                        IGDB.platforms.find { it.id == platformID }?.name?.contains(searchQuery, ignoreCase = true) == true
                                    }) && (if (isFavoriteList) favoriteStates[game.id] == true else true)
                        }
                    }
                )
            }

            //Affichage des jeux en fonction des filtres et redirection avec le navController
            if (filteredGames.isNotEmpty()) {
                LazyColumn {
                    items(filteredGames) { game ->
                        val isFavorite = favoriteStates[game.id] == true
                        GameCard(
                            game,
                            onGameClick = {
                                navController.navigate(GameDetail(game.id))
                            },
                            context = context,
                            isFavorite = isFavorite,
                            onFavoriteChange = { newFavoriteState ->
                                favoriteStates[game.id] = newFavoriteState

                                if (isFavoriteList) {
                                    val hasFavorite = favoriteStates.values.any { it }

                                    if (!hasFavorite) {
                                        isFavoriteList = false
                                    }

                                    onFilterChange(
                                        IGDB.games.filter { game ->
                                            (searchQuery.isBlank() ||
                                                    game.name.contains(
                                                        searchQuery,
                                                        ignoreCase = true
                                                    ) ||
                                                    game.genres.any { genreId ->
                                                        IGDB.genres.find { it.id == genreId }?.name?.contains(
                                                            searchQuery,
                                                            ignoreCase = true
                                                        ) == true
                                                    } ||
                                                    game.platforms.any { platformID ->
                                                        IGDB.platforms.find { it.id == platformID }?.name?.contains(
                                                            searchQuery,
                                                            ignoreCase = true
                                                        ) == true
                                                    }) && (if (isFavorite) favoriteStates[game.id] == true else true)
                                        }
                                    )
                                }
                            })
                    }
                }
            } else {
                Text("No Match :(")
            }
        }
    }
}