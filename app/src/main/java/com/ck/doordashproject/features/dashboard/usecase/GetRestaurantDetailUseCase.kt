package com.ck.doordashproject.features.dashboard.usecase

import android.os.Parcelable
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDetailDataModel
import com.ck.doordashproject.base.usecase.SingleUseCase
import com.ck.doordashproject.features.dashboard.models.repository.network.RestaurantInteractors
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

@ViewModelScoped
class GetRestaurantDetailUseCase @Inject constructor(
    private val interactor: RestaurantInteractors
) : SingleUseCase<GetRestaurantDetailUseCase.Input, RestaurantDetailDataModel> {

    override fun getSingle(input: Input): Single<RestaurantDetailDataModel> = interactor
        .getRestaurantDetail(
            restaurantId = input.restaurantId
        )
        .subscribeOn(Schedulers.io())

    @Parcelize
    data class Input(val restaurantId: Long): Parcelable
}