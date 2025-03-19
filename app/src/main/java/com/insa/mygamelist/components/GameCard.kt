package com.insa.mygamelist.components

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.insa.mygamelist.data.Game
import com.insa.mygamelist.data.IGDB
@Composable
fun GameCard(game: Game, onGameClick: (Int) -> Unit, context: Context, isFavorite: Boolean, onFavoriteChange: (Boolean) -> Unit) {
    val darkTheme = isSystemInDarkTheme()

    // Définir les couleurs pour les modes clair et sombre
    val cardBackgroundColor = if (darkTheme) Color(0xFF1F1F1F) else Color(0xFFEAE6F2) // Fond de carte
    val textColor = if (darkTheme) Color.White else Color.Black // Texte
    val subtitleColor = if (darkTheme) Color.LightGray else Color.Gray // Sous-titres (Genres)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { onGameClick(game.id.toInt()) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor) // Appliquer la couleur dynamique de fond
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val coverUrl = IGDB.covers.find { cover -> cover.id == game.cover }?.url
            val imageUrl = if (coverUrl != null) "https:$coverUrl" else null
            AsyncImage(
                model = imageUrl ?: "https://upload.wikimedia.org/wikipedia/commons/0/01/Gaming.png",
                contentDescription = game.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(7.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append(game.name)
                        }
                    },
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = textColor // Changer la couleur du texte en fonction du thème
                    )
                )
                Row {
                    val genres = game.genres.mapNotNull { genreId ->
                        IGDB.genres.find { it.id == genreId }?.name
                    }.joinToString(", ").ifEmpty { "Inconnu" }

                    Text(
                        text = "Genres : $genres",
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = subtitleColor // Changer la couleur du sous-titre (genres) en fonction du thème
                    )
                }
            }
            FavoriteButton(game, context, isFavorite, onFavoriteChange)
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}
