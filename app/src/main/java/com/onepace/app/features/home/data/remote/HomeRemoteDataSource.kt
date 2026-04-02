package com.onepace.app.features.home.data.remote

import com.onepace.app.core.di.TmdbClient
import com.onepace.app.core.network.ApiConstants
import com.onepace.app.features.home.data.remote.dto.SeasonDetailDto
import com.onepace.app.features.home.data.remote.dto.SeriesDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    @TmdbClient private val client: HttpClient
) {

    suspend fun fetchSeries(): SeriesDto {
        return client.get("tv/${ApiConstants.ONE_PIECE_ID}") {
            parameter("language", "pt-BR")
        }.body()
    }

    suspend fun fetchSeasonDetail(seasonNumber: Int): SeasonDetailDto {
        return client.get("tv/${ApiConstants.ONE_PIECE_ID}/season/$seasonNumber") {
            parameter("language", "pt-BR")
        }.body()
    }
}
