package com.onepace.app.features.home.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonDetailDto(
    val id: Int,
    val name: String,
    val overview: String = "",
    @SerialName("season_number") val seasonNumber: Int,
    @SerialName("poster_path") val posterPath: String? = null,
    val episodes: List<EpisodeDto> = emptyList()
)
