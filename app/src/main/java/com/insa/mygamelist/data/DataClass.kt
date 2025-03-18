package com.insa.mygamelist.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Cover(val id: Long, val url: String)
@Serializable
data class Game(val id: Long, val cover: Long, @SerialName ("first_release_date") val firstReleasedDate: Long, val genres: List<Long>, val name: String, val platforms: List<Int>, val summary: String, @SerialName ("total_rating") val totalRating: Double)
@Serializable
data class Genre(val id: Long, val name: String)
@Serializable
data class PlatformLogo(val id: Long, val url: String)
@Serializable
data class Platform(val id: Int, val name: String, @SerialName ("platform_logo") val platformLogo: Long?)
