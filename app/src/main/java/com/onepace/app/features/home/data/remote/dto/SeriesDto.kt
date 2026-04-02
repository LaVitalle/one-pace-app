package com.onepace.app.features.home.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeriesDto(
    val id: Int,
    val name: String,
    val overview: String,
    val tagline: String = "",
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("number_of_seasons") val numberOfSeasons: Int = 0,
    @SerialName("number_of_episodes") val numberOfEpisodes: Int = 0,
    @SerialName("first_air_date") val firstAirDate: String = "",
    val status: String = "",
    val seasons: List<SeasonSummaryDto> = emptyList()
)
