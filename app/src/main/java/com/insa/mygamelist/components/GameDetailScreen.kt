package com.insa.mygamelist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.insa.mygamelist.data.IGDB


//Fonction d'affichage du détail des jeux

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GameDetailScreen(id: Long, innerPadding: PaddingValues) {

    val game = IGDB.games.find { it.id == id }?: return
    //Gestion du thème sur le téléphone
    val darkTheme = isSystemInDarkTheme()
    val cardBackgroundColor =
        if (darkTheme) Color(0xFF1F1F1F) else Color(0xFFEAE6F2) // Fond de carte

    //Mise en place d'un padding pour que se soit plus joli
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
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .padding(15.dp)
        ) {
            items(game.platforms) { idPlatform ->
                val logo = IGDB.platforms.find { it.id == idPlatform }?.platform_logo
                AsyncImage(
                    model = "https:${
                        IGDB.platformLogos.find { it.id == logo }?.url?.replace(
                            "jpg",
                            "png"
                        )
                    }",
                    contentDescription = IGDB.platforms.find { it.id == idPlatform }?.name,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(cardBackgroundColor)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Résumé : " + game.summary)
    }
}
