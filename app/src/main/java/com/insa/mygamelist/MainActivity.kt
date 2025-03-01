package com.insa.mygamelist

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.insa.mygamelist.data.Game
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.ui.theme.MyGamesListTheme
import kotlinx.serialization.Serializable

@Serializable
object GameList

@Serializable
data class GameDetail(val gameId: Long)

// Carte de jeu
@Composable
fun GameCard(game: Game, onGameClick: (Int) -> Unit, context: Context, isFavorite: Boolean, onFavoriteChange: (Boolean) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { onGameClick(game.id.toInt()) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAE6F2))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = "https:" + IGDB.covers.find { cover -> cover.id == game.cover }?.url,
                contentDescription = game.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(7.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column (modifier = Modifier.weight(1f)){
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append(game.name)
                        }
                    },
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
                Row {
                    val genres = game.genres.mapNotNull { genreId ->
                        IGDB.genres.find { it.id == genreId }?.name
                    }.joinToString(", ")

                    Text(
                        text = "Genres : $genres",
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            FavoriteButton(game, context, isFavorite, onFavoriteChange)
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}


//Composant principal avec navigation
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

// Liste des jeux
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GameListScreen(navController: NavController, context: Context) {

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isSearching by rememberSaveable { mutableStateOf(false) }

    val favoriteStates = remember {
        mutableStateMapOf<Long, Boolean>().apply {
            putAll(IGDB.games.associate { it.id to IGDB.isFavorite(it) })
        }
    }



    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color(0xFFFF8C00),
                    titleContentColor = Color.Black,
                ),
                title = {
                    if (isSearching) {
                        Row (modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                label = { Text("Recherche un jeu ...") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                            )
                            IconButton(onClick = { isSearching = false; searchQuery = "" }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Fermer la recherche"
                                )
                            }
                        }
                    } else {
                        Text("My Games List")
                    }
                },
                actions = {
                    if (!isSearching) {
                        IconButton(onClick = { isSearching = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Rechercher")
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

            val filteredGames = IGDB.games.filter { game ->
                game.name.contains(searchQuery, ignoreCase = true) ||
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
                            ) ==true
                        }
            }
            if (filteredGames.isNotEmpty()) {
                LazyColumn {
                    items(filteredGames) { game ->
                        val isFavorite = favoriteStates[game.id] ?: false
                        GameCard(game, onGameClick =  {
                            navController.navigate(GameDetail(game.id))
                        }, context = context, isFavorite = isFavorite, onFavoriteChange = { newFavoriteState ->
                            favoriteStates[game.id] = newFavoriteState
                        })
                    }
                }
            }else {
                    Text("No Match :(")
            }
        }
    }
}

// Ecran de détail du jeu
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GameDetailScreen(navController: NavController, gameDetail: GameDetail?, context: Context) {
    val game = IGDB.games.find { it.id == gameDetail?.gameId }?: return

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
                            contentDescription = "Retour"
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        val modifiedPadding = PaddingValues(
            start = innerPadding.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
            top = innerPadding.calculateTopPadding() + 16.dp,
            end = innerPadding.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
            bottom = innerPadding.calculateBottomPadding() + 16.dp
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
                .padding(modifiedPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append(game.name)
                    }
                },
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            AsyncImage(
                model = "https:" + IGDB.covers.find { it.id == game.cover }?.url,
                contentDescription = game.name,
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = game.genres.joinToString(", ") { genreId ->
                    IGDB.genres.find { it.id == genreId }?.name ?: "Inconnu"
                },
                style = TextStyle(
                    fontStyle = FontStyle.Italic,
                    fontSize = 12.sp
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                items(game.platforms) { idPlatform ->
                    val logo = IGDB.platforms.find { it.id == idPlatform}?.platformLogo
                    AsyncImage(
                        model = "https:" + IGDB.platform_logos.find { it.id == logo }?.url,
                        contentDescription = IGDB.platforms.find { it.id == idPlatform}?.name,
                        modifier = Modifier
                            .size(75.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Résumé : " + game.summary)
        }
    }
}

@Composable
fun FavoriteButton(game: Game, context: Context, isFavorite: Boolean, onFavoriteChange: (Boolean) -> Unit) {
    IconButton(
        onClick = {
            IGDB.toggleFavorite(context, game)
            onFavoriteChange(!isFavorite)
        }
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
            contentDescription = if (isFavorite) "Retirer des favoris" else "Ajouter aux favoris",
            tint = if (isFavorite) Color.Yellow else Color.LightGray
        )
    }
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        IGDB.load(this)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            MyGamesListTheme {
                AppNavigation(navController, this)
            }
        }
    }
}