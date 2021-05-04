package com.ck.doordashproject.features.dashboard.usecase

import android.os.Parcelable
import com.ck.doordashproject.base.repository.database.LikedDataDao
import com.ck.doordashproject.base.repository.database.entity.LikedDb
import com.ck.doordashproject.base.usecase.CoroutineUseCase
import com.ck.doordashproject.base.usecase.DispatchersContract
import com.ck.doordashproject.features.dashboard.data.LikedStatus
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class LikeRestaurantUseCase @Inject constructor(
    private val dataDao: LikedDataDao
): CoroutineUseCase<LikeRestaurantUseCase.Input, Unit> {

    override suspend fun invoke(input: Input, dispatchersContext: DispatchersContract) = withContext(dispatchersContext.IO) {
        dataDao.insert(LikedDb(
            id = input.restaurantId,
            likedStatus = input.statuts
        ))
    }

    @Parcelize
    data class Input(val restaurantId: Long, val statuts: LikedStatus): Parcelable
}