package com.ck.doordashproject.features.dashboard.usecase

import android.os.Parcelable
import com.ck.doordashproject.base.di.DispatcherIO
import com.ck.doordashproject.base.repository.database.LikedDataDao
import com.ck.doordashproject.base.repository.database.entity.LikedDb
import com.ck.doordashproject.base.usecase.CoroutineUseCase
import com.ck.doordashproject.features.dashboard.data.LikedStatus
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class LikeRestaurantUseCase @Inject constructor(
    @DispatcherIO private val ioDispatcherMain: CoroutineDispatcher,
    private val dataDao: LikedDataDao
): CoroutineUseCase<LikeRestaurantUseCase.Input, Unit> {
    override suspend fun invoke(
        input: Input,
        onResultFn: (Unit) -> Unit
    ) {
        withContext(ioDispatcherMain) {
            dataDao.insert(LikedDb(
                id = input.restaurantId,
                likedStatus = input.statuts
            ))
        }

    }

    @Parcelize
    data class Input(val restaurantId: Long, val statuts: LikedStatus): Parcelable
}