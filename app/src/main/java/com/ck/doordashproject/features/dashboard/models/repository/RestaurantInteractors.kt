package com.ck.doordashproject.features.dashboard.models.repository

import androidx.annotation.NonNull
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDataModel
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDetailDataModel
import io.reactivex.Observable

interface RestaurantInteractors {
    @NonNull
    fun getRestaurantNearBy(lat: Float, lng: Float): Observable<ArrayList<RestaurantDataModel>>

    @NonNull
    fun getRestaurantDetail(restaurantId: Long): Observable<RestaurantDetailDataModel>
}