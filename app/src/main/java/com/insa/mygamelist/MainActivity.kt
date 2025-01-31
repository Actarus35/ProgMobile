package com.insa.mygamelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.ui.theme.MyGamesListTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        IGDB.load(this)

        enableEdgeToEdge()
        setContent {
            MyGamesListTheme {
                Scaffold(topBar = {
                    TopAppBar(colors = topAppBarColors(
                        containerColor = Color.Magenta,
                        titleContentColor = Color.Black,
                    ), title = { Text("My Games List") })
                }, modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LazyColumn(modifier = Modifier.padding(innerPadding)) {
                        items(IGDB.games) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFEAE6F2))
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = "https:" + IGDB.covers.find { cover -> cover.id == it.cover }?.url,
                                        contentDescription = it.name,
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
                                                    append(it.name)
                                                }
                                            },
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp
                                            )
                                        )
                                        Row() {
                                            var res: String = ""
                                            for (i in it.genres) {
                                                for (x in 0 until IGDB.genres.size) {
                                                    if (i == IGDB.genres[x].id)
                                                        res += IGDB.genres[x].name + ", "
                                                }
                                            }
                                            val newRes = res.dropLast(2)
                                            Text(
                                                text = "Genres : $newRes",
                                                fontSize = 14.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}