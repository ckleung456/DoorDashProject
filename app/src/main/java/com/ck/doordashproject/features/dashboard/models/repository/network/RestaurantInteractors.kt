package com.ck.doordashproject.features.dashboard.models.repository.network

import com.ck.doordashproject.base.models.data.restaurants.RestaurantDataModel
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDetailDataModel
import com.ck.doordashproject.base.repository.network.DoorDashAPIs
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class RestaurantInteractors @Inject constructor(
    private val apiService: DoorDashAPIs
) {
    companion object {
        const val OFFSET = 0
        const val LIMIT = 50
    }

    fun getRestaurantNearBy(
        lat: Float,
        lng: Float
    ): Flow<List<RestaurantDataModel>> = apiService.fetchRestaurantNearBy(
        lat = lat,
        lng = lng,
        offset = OFFSET,
        limit = LIMIT
    )

    fun getRestaurantDetail(restaurantId: Long): Flow<RestaurantDetailDataModel> =
        apiService.fetchRestaurantDetail(
            id = restaurantId
        )
}