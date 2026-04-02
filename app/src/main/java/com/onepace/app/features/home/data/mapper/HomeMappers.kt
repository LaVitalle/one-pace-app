package com.onepace.app.features.home.data.mapper

import com.onepace.app.core.network.ApiConstants
import com.onepace.app.features.home.data.remote.dto.EpisodeDto
import com.onepace.app.features.home.data.remote.dto.SeasonSummaryDto
import com.onepace.app.features.home.data.remote.dto.SeriesDto
import com.onepace.app.features.home.domain.model.Episode
import com.onepace.app.features.home.domain.model.Season
import com.onepace.app.features.home.domain.model.Series

fun SeriesDto.toDomain(): Series = Series(
    id = id,
    name = name,
    overview = overview,
    tagline = tagline,
    backdropUrl = backdropPath?.let { ApiConstants.backdropUrl(it) },
    posterUrl = posterPath?.let { ApiConstants.posterUrl(it) },
    voteAverage = voteAverage,
    numberOfSeasons = numberOfSeasons,
    numberOfEpisodes = numberOfEpisodes,
    firstAirDate = firstAirDate,
    seasons = seasons
        .filter { it.seasonNumber > 0 }
        .map { it.toDomain() }
)

fun SeasonSummaryDto.toDomain(): Season = Season(
    id = id,
    name = name,
    overview = overview,
    seasonNumber = seasonNumber,
    episodeCount = episodeCount,
    posterUrl = posterPath?.let { ApiConstants.posterUrl(it) },
    voteAverage = voteAverage
)

fun EpisodeDto.toDomain(): Episode = Episode(
    id = id,
    episodeNumber = episodeNumber,
    name = name,
    overview = overview,
    stillUrl = stillPath?.let { ApiConstants.stillUrl(it) },
    runtime = runtime,
    airDate = airDate,
    voteAverage = voteAverage,
    seasonNumber = seasonNumber
)
