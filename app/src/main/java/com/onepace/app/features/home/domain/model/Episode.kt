package com.onepace.app.features.home.domain.model

data class Episode(
    val id: Int,
    val episodeNumber: Int,
    val name: String,
    val overview: String,
    val stillUrl: String?,
    val runtime: Int?,
    val airDate: String?,
    val voteAverage: Double,
    val seasonNumber: Int
)
