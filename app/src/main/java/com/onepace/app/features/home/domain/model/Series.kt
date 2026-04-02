package com.onepace.app.features.home.domain.model

data class Series(
    val id: Int,
    val name: String,
    val overview: String,
    val tagline: String,
    val backdropUrl: String?,
    val posterUrl: String?,
    val voteAverage: Double,
    val numberOfSeasons: Int,
    val numberOfEpisodes: Int,
    val firstAirDate: String,
    val seasons: List<Season>
)
