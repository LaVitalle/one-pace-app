package com.onepace.app.features.home.presentation

import com.onepace.app.features.home.domain.model.Episode
import com.onepace.app.features.home.domain.model.Season
import com.onepace.app.features.home.domain.model.Series

data class HomeUiState(
    val series: Series? = null,
    val seasons: List<Season> = emptyList(),
    val selectedSeason: Season? = null,
    val episodes: List<Episode> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingEpisodes: Boolean = false,
    val errorMessage: String? = null
)

sealed interface HomeEvent {
    data class OnSeasonClick(val season: Season) : HomeEvent
    data class OnEpisodeClick(val episode: Episode) : HomeEvent
    data object OnRetry : HomeEvent
}
