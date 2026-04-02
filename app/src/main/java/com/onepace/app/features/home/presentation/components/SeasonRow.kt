package com.onepace.app.features.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onepace.app.R
import com.onepace.app.core.ui.theme.OnePieceTheme
import com.onepace.app.features.home.domain.model.Season

@Composable
fun SeasonRow(
    seasons: List<Season>,
    selectedSeason: Season?,
    onSeasonClick: (Season) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.home_arcs_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = seasons,
                key = { it.id }
            ) { season ->
                SeasonCard(
                    name = season.name,
                    episodeCount = season.episodeCount,
                    posterUrl = season.posterUrl,
                    isSelected = season == selectedSeason,
                    onClick = { onSeasonClick(season) }
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun SeasonRowPreview() {
    OnePieceTheme {
        val seasons = listOf(
            Season(
                id = 1,
                name = "East Blue",
                overview = "",
                seasonNumber = 1,
                episodeCount = 61,
                posterUrl = null,
                voteAverage = 8.5
            ),
            Season(
                id = 2,
                name = "Alabasta",
                overview = "",
                seasonNumber = 2,
                episodeCount = 39,
                posterUrl = null,
                voteAverage = 8.7
            ),
            Season(
                id = 3,
                name = "Sky Island",
                overview = "",
                seasonNumber = 3,
                episodeCount = 43,
                posterUrl = null,
                voteAverage = 8.3
            )
        )
        SeasonRow(
            seasons = seasons,
            selectedSeason = seasons[0],
            onSeasonClick = {}
        )
    }
}
