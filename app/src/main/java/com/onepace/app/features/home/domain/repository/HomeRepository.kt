package com.onepace.app.features.home.domain.repository

import com.onepace.app.core.data.Resource
import com.onepace.app.features.home.domain.model.Episode
import com.onepace.app.features.home.domain.model.Series
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getSeries(): Flow<Resource<Series>>
    fun getSeasonEpisodes(seasonNumber: Int): Flow<Resource<List<Episode>>>
}
