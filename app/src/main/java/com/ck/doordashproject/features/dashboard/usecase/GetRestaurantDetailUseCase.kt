package com.ck.doordashproject.features.dashboard.usecase

import android.os.Parcelable
import com.ck.doordashproject.base.di.DispatcherIO
import com.ck.doordashproject.base.di.DispatcherMain
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDetailDataModel
import com.ck.doordashproject.base.usecase.FlowUseCase
import com.ck.doordashproject.features.dashboard.models.repository.network.RestaurantInteractors
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ViewModelScoped
class GetRestaurantDetailUseCase @Inject constructor(
    @DispatcherMain private val mainDispatcherMain: CoroutineDispatcher,
    @DispatcherIO private val ioDispatcherIO: CoroutineDispatcher,
    private val interactor: RestaurantInteractors
) : FlowUseCase<GetRestaurantDetailUseCase.Input, RestaurantDetailDataModel>(dispatcherMain = mainDispatcherMain) {

    override suspend fun getFlow(
        input: Input
    ): Flow<RestaurantDetailDataModel>  = interactor
        .getRestaurantDetail(
            restaurantId = input.restaurantId
        )
        .flowOn(ioDispatcherIO)

    @Parcelize
    data class Input(val restaurantId: Long): Parcelable
}