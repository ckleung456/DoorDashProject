package com.ck.doordashproject.features.dashboard.usecase

import android.os.Parcelable
import android.util.Log
import com.ck.doordashproject.base.repository.database.LikedDataDao
import com.ck.doordashproject.base.usecase.SingleUseCase
import com.ck.doordashproject.features.dashboard.data.LikedStatus
import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper
import com.ck.doordashproject.features.dashboard.models.repository.network.RestaurantInteractors
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

@ViewModelScoped
class GetNearByRestaurantsUseCase @Inject constructor(
    private val interactor: RestaurantInteractors,
    private val likedDataDao: LikedDataDao
): SingleUseCase<GetNearByRestaurantsUseCase.Input, List<RestaurantDataModelWrapper>> {

    override fun getSingle(input: Input): Single<List<RestaurantDataModelWrapper>>  =
        interactor.getRestaurantNearBy(
            lat = input.lat,
            lng = input.lng
        )
            .map { data ->
                data.map { dataModel ->
                    val entity = likedDataDao.getById(dataModel.id)
                    entity?.let {
                        RestaurantDataModelWrapper(dataModel, entity.likedStatus)
                    } ?: RestaurantDataModelWrapper(dataModel, LikedStatus.NO_PREF)
                }
            }
            .subscribeOn(Schedulers.io())

    @Parcelize
    data class Input(val lat: Float, val lng: Float): Parcelable
}