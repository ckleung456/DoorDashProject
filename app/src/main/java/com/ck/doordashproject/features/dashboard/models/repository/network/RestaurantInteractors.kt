package com.ck.doordashproject.features.dashboard.models.repository.network

import androidx.annotation.NonNull
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDataModel
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDetailDataModel
import io.reactivex.Observable
import io.reactivex.Single

interface RestaurantInteractors {
    @NonNull
    fun getRestaurantNearBy(lat: Float, lng: Float): Single<List<RestaurantDataModel>>

    @NonNull
    fun getRestaurantDetail(restaurantId: Long): Single<RestaurantDetailDataModel>
}