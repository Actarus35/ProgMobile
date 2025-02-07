package com.insa.mygamelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil3.compose.AsyncImage
import com.insa.mygamelist.data.Game
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.ui.theme.MyGamesListTheme
import kotlinx.serialization.Serializable

sealed class Screen(val route: String) {
    object GameList : Screen("gameList")
    object GameDetail : Screen("gameDetail/{gameId}") {
        fun createRoute(gameId: Any?) = "gameDetail/$gameId"
    }
}


// Carte de jeu
@Composable
fun GameCard(game: Game, onGameClick: (Int) -> Unit) {
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
            Column() {
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
                Row() {
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
        }
    }
}


//Composant principal avec navigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(navController: NavHostController, innerPadding: PaddingValues) {

    NavHost(navController, startDestination = Screen.GameList.route) {
        composable(Screen.GameList.route) {
            GameListScreen(navController, innerPadding)
        }
        composable(
            Screen.GameDetail.route,
            arguments = listOf(navArgument("gameId") { type = NavType.IntType })
        ) { backStachEntry ->
            val gameId = backStachEntry.arguments?.getInt("gameId")
            GameDetailScreen(navController, gameId, innerPadding)
        }
    }
}

// Liste des jeux
@Composable
fun GameListScreen(navController: NavController, innerPadding: PaddingValues) {
    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        items(IGDB.games) { game ->
            GameCard(game = game, onGameClick = { gameId ->
                navController.navigate(Screen.GameDetail.createRoute(gameId))
            })
        }
    }
}

// Ecran de détail du jeu
@Composable
fun GameDetailScreen(navController: NavController, gameId: Int?, innerPadding: PaddingValues) {
    val game = IGDB.games.find { it.id == gameId?.toLong() }

    if (game == null) {
        Text("Jeu non trouvé", modifier = Modifier.padding(16.dp))
        return
    }

    val modifiedPadding = PaddingValues(
        start = innerPadding.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
        top = innerPadding.calculateTopPadding() + 16.dp,
        end = innerPadding.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
        bottom = innerPadding.calculateBottomPadding() + 16.dp
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(modifiedPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = "https:" + IGDB.covers.find { it.id == game.cover }?.url,
            contentDescription = game.name,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Genres : " + game.genres.joinToString(", ") { genreId ->
            IGDB.genres.find { it.id == genreId }?.name ?: "Inconnu"
        })
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Résumé : " + game.summary)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Retour")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        IGDB.load(this)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            MyGamesListTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = topAppBarColors(
                                containerColor = Color(0xFFFF8C00),
                                titleContentColor = Color.Black,
                            ),
                            title = { Text("My Games List") }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AppNavigation(navController, innerPadding)
                }
            }
        }
    }
}

