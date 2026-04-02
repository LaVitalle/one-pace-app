package com.onepace.app.features.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onepace.app.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onepace.app.core.ui.theme.OnePieceTheme
import com.onepace.app.features.home.domain.model.Episode
import com.onepace.app.features.home.domain.model.Season
import com.onepace.app.features.home.domain.model.Series
import com.onepace.app.features.home.presentation.components.EpisodeCard
import com.onepace.app.features.home.presentation.components.HeroBanner
import com.onepace.app.features.home.presentation.components.SeasonRow

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToPlayer: (Episode) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                is HomeEvent.OnEpisodeClick -> onNavigateToPlayer(event.episode)
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            uiState.errorMessage != null -> {
                ErrorContent(
                    message = uiState.errorMessage,
                    onRetry = { onEvent(HomeEvent.OnRetry) },
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            uiState.series != null -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item(key = "hero") {
                        HeroBanner(
                            title = uiState.series.name,
                            tagline = uiState.series.tagline,
                            backdropUrl = uiState.series.backdropUrl,
                            voteAverage = uiState.series.voteAverage
                        )
                    }

                    item(key = "hero_spacer") {
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    if (uiState.seasons.isNotEmpty()) {
                        item(key = "seasons") {
                            SeasonRow(
                                seasons = uiState.seasons,
                                selectedSeason = uiState.selectedSeason,
                                onSeasonClick = { season ->
                                    onEvent(HomeEvent.OnSeasonClick(season))
                                }
                            )
                        }

                        item(key = "seasons_spacer") {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }

                    item(key = "episodes_title") {
                        Text(
                            text = stringResource(R.string.home_episodes_title),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if (uiState.isLoadingEpisodes) {
                        item(key = "episodes_loading") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    items(
                        items = uiState.episodes,
                        key = { it.id }
                    ) { episode ->
                        EpisodeCard(
                            episodeNumber = episode.episodeNumber,
                            name = episode.name,
                            stillUrl = episode.stillUrl,
                            runtime = episode.runtime,
                            onClick = { onEvent(HomeEvent.OnEpisodeClick(episode)) },
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 6.dp
                            )
                        )
                    }

                    item(key = "bottom_spacer") {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = stringResource(R.string.home_error_retry),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun HomeContentLoadingPreview() {
    OnePieceTheme {
        HomeContent(
            uiState = HomeUiState(isLoading = true),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun HomeContentErrorPreview() {
    OnePieceTheme {
        HomeContent(
            uiState = HomeUiState(errorMessage = "Falha ao carregar. Verifique sua conexao."),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun HomeContentSuccessPreview() {
    val seasons = listOf(
        Season(1, "East Blue", "", 1, 61, null, 8.5),
        Season(2, "Alabasta", "", 2, 39, null, 8.7),
        Season(3, "Sky Island", "", 3, 43, null, 8.3)
    )
    val episodes = listOf(
        Episode(1, 1, "Romance Dawn", "", null, 24, "1999-10-20", 8.5, 1),
        Episode(2, 2, "Enter the Great Swordsman!", "", null, 24, "1999-11-17", 8.3, 1),
        Episode(3, 3, "Morgan vs. Luffy", "", null, 24, "1999-11-24", 8.1, 1)
    )
    OnePieceTheme {
        HomeContent(
            uiState = HomeUiState(
                series = Series(
                    id = 1,
                    name = "One Piece",
                    overview = "A long running anime...",
                    tagline = "Wealth, fame, power.",
                    backdropUrl = null,
                    posterUrl = null,
                    voteAverage = 8.7,
                    numberOfSeasons = 21,
                    numberOfEpisodes = 1100,
                    firstAirDate = "1999-10-20",
                    seasons = seasons
                ),
                seasons = seasons,
                selectedSeason = seasons[0],
                episodes = episodes
            ),
            onEvent = {}
        )
    }
}
