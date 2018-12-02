package com.ck.doordashproject.features.dashboard.modules.repository

import androidx.annotation.NonNull
import com.ck.doordashproject.base.modules.data.RestaurantDataModel
import com.ck.doordashproject.base.modules.data.RestaurantDetailDataModel
import io.reactivex.Observable

interface RestaurantInteractors {
    @NonNull
    fun getRestaurantNearBy(lat: Float, lng: Float): Observable<ArrayList<RestaurantDataModel>>

    @NonNull
    fun getRestaurantDetail(restaurantId: Long): Observable<RestaurantDetailDataModel>
}