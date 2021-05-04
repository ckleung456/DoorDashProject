package com.ck.doordashproject.features.dashboard.di

import com.ck.doordashproject.features.dashboard.models.repository.network.RestaurantInteractors
import com.ck.doordashproject.features.dashboard.models.repository.network.RestaurantInteractorsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
abstract class DashboardInteractorModule {
    @ViewModelScoped
    @Binds
    abstract fun bindRestaurantDashboardInteractor(impl: RestaurantInteractorsImpl): RestaurantInteractors
}