@file:Suppress("PropertyName")

package com.insa.mygamelist.data

import kotlinx.serialization.Serializable


data class Cover(val id: Long, val url: String)
@Serializable
data class Game(val id: Long, val cover: Long, val first_release_date: Long, val genres: List<Long>, val name: String, val platforms: List<Int>, val summary: String, val total_rating: Double)

data class Genre(val id: Long, val name: String)

data class PlatformLogo(val id: Long, val url: String)
@Serializable
data class Platform(val id: Int, val name: String, val platform_logo: Long)