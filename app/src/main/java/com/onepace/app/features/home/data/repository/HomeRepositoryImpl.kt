package com.onepace.app.features.home.data.repository

import com.onepace.app.core.data.Resource
import com.onepace.app.features.home.data.mapper.toDomain
import com.onepace.app.features.home.data.remote.HomeRemoteDataSource
import com.onepace.app.features.home.domain.model.Episode
import com.onepace.app.features.home.domain.model.Series
import com.onepace.app.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val remoteDataSource: HomeRemoteDataSource
) : HomeRepository {

    override fun getSeries(): Flow<Resource<Series>> = flow {
        emit(Resource.Loading)
        try {
            val series = remoteDataSource.fetchSeries().toDomain()
            emit(Resource.Success(series))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Erro ao carregar dados da série", e))
        }
    }

    override fun getSeasonEpisodes(seasonNumber: Int): Flow<Resource<List<Episode>>> = flow {
        emit(Resource.Loading)
        try {
            val episodes = remoteDataSource.fetchSeasonDetail(seasonNumber)
                .episodes
                .map { it.toDomain() }
            emit(Resource.Success(episodes))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Erro ao carregar episódios", e))
        }
    }
}
