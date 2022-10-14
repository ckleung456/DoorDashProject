package com.ck.doordashproject.features.dashboard.usecase

import android.os.Parcelable
import com.ck.doordashproject.base.di.DispatcherIO
import com.ck.doordashproject.base.di.DispatcherMain
import com.ck.doordashproject.base.repository.database.LikedDataDao
import com.ck.doordashproject.base.usecase.FlowUseCase
import com.ck.doordashproject.features.dashboard.data.LikedStatus
import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper
import com.ck.doordashproject.features.dashboard.models.repository.network.RestaurantInteractors
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class GetNearByRestaurantsUseCase @Inject constructor(
    @DispatcherMain private val mainDispatcherMain: CoroutineDispatcher,
    @DispatcherIO private val ioDispatcherIO: CoroutineDispatcher,
    private val interactor: RestaurantInteractors,
    private val likedDataDao: LikedDataDao
): FlowUseCase<GetNearByRestaurantsUseCase.Input, List<RestaurantDataModelWrapper>>(dispatcherMain = mainDispatcherMain) {

    override suspend fun getFlow(input: Input): Flow<List<RestaurantDataModelWrapper>> = interactor.getRestaurantNearBy(
        lat = input.lat,
        lng = input.lng
    ).map { data ->
        data.map { dataModel ->
            val entity = likedDataDao.getById(dataModel.id)
            entity?.let {
                RestaurantDataModelWrapper(dataModel, entity.likedStatus)
            } ?: RestaurantDataModelWrapper(dataModel, LikedStatus.NO_PREF)
        }
    }
        .flowOn(ioDispatcherIO)


    @Parcelize
    data class Input(val lat: Float, val lng: Float): Parcelable
}