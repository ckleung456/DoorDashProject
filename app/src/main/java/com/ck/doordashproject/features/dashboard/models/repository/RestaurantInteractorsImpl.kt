package com.ck.doordashproject.features.dashboard.models.repository

import androidx.annotation.VisibleForTesting
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDataModel
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDetailDataModel
import com.ck.doordashproject.base.network.DoorDashService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RestaurantInteractorsImpl : RestaurantInteractors {
    companion object {
        const val OFFSET = 0
        const val LIMIT = 50
    }

    private val mService: DoorDashService

    constructor() : this(DoorDashService.INSTANCE)

    @VisibleForTesting
    constructor(service: DoorDashService) {
        mService = service
    }

    override fun getRestaurantNearBy(lat: Float, lng: Float): Observable<ArrayList<RestaurantDataModel>> {
        return mService.service.fetchRestaurantNearBy(lat, lng, OFFSET, LIMIT)
                .map { response ->
                    val list = ArrayList<RestaurantDataModel>()
                    list.addAll(response.body()!!)
                    list
                }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun getRestaurantDetail(restaurantId: Long): Observable<RestaurantDetailDataModel> {
        return mService.service.fetchRestaurantDetail(restaurantId)
                .map { response ->
                    if (response.body() == null) {
                        RestaurantDetailDataModel(
                            Long.MIN_VALUE,
                            "",
                            Long.MIN_VALUE,
                            Double.MIN_VALUE,
                            "",
                            "",
                            Double.MIN_VALUE,
                            "",
                            "",
                            ""
                        )
                    } else {
                        response.body()!!
                    }
                }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }
}