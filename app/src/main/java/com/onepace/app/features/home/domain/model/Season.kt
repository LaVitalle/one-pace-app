package com.onepace.app.features.home.domain.model

data class Season(
    val id: Int,
    val name: String,
    val overview: String,
    val seasonNumber: Int,
    val episodeCount: Int,
    val posterUrl: String?,
    val voteAverage: Double
)
