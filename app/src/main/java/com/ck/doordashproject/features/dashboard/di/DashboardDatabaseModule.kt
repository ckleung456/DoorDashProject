package com.ck.doordashproject.features.dashboard.di

import com.ck.doordashproject.base.repository.database.LikedDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object DashboardDatabaseModule {
    @Provides
    fun provideLikedDao(database: LikedDatabase) = database.getLikedDataDao()
}