package com.onepace.app.features.home.data.di

import com.onepace.app.features.home.data.repository.HomeRepositoryImpl
import com.onepace.app.features.home.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class HomeModule {

    @Binds
    @ViewModelScoped
    abstract fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository
}
