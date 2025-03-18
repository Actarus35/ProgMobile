package com.insa.mygamelist.components

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.insa.mygamelist.data.Game
import com.insa.mygamelist.data.IGDB

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