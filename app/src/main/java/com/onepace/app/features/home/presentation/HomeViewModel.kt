package com.onepace.app.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onepace.app.core.data.Resource
import com.onepace.app.features.home.domain.model.Season
import com.onepace.app.features.home.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadSeries()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnSeasonClick -> selectSeason(event.season)
            is HomeEvent.OnEpisodeClick -> { /* Navegação para player será implementada depois */ }
            is HomeEvent.OnRetry -> loadSeries()
        }
    }

    private fun loadSeries() {
        homeRepository.getSeries().onEach { resource ->
            when (resource) {
                is Resource.Loading -> _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                is Resource.Success -> {
                    val series = resource.data
                    val firstSeason = series.seasons.firstOrNull()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            series = series,
                            seasons = series.seasons,
                            selectedSeason = firstSeason
                        )
                    }
                    firstSeason?.let { loadEpisodes(it) }
                }
                is Resource.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = resource.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun selectSeason(season: Season) {
        if (season == _uiState.value.selectedSeason) return
        _uiState.update { it.copy(selectedSeason = season, episodes = emptyList()) }
        loadEpisodes(season)
    }

    private fun loadEpisodes(season: Season) {
        homeRepository.getSeasonEpisodes(season.seasonNumber).onEach { resource ->
            when (resource) {
                is Resource.Loading -> _uiState.update { it.copy(isLoadingEpisodes = true) }
                is Resource.Success -> _uiState.update {
                    it.copy(isLoadingEpisodes = false, episodes = resource.data)
                }
                is Resource.Error -> _uiState.update {
                    it.copy(isLoadingEpisodes = false)
                }
            }
        }.launchIn(viewModelScope)
    }
}
