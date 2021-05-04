package com.ck.doordashproject.features.dashboard.models.repository.network

import com.ck.doordashproject.base.models.data.restaurants.RestaurantDataModel
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDetailDataModel
import com.ck.doordashproject.base.repository.network.DoorDashAPIs
import com.ck.doordashproject.base.repository.network.RetrofitException
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.Single
import java.lang.Exception
import java.lang.RuntimeException
import javax.inject.Inject

@ViewModelScoped
class RestaurantInteractorsImpl @Inject constructor(
    private val apiService: DoorDashAPIs
) : RestaurantInteractors {
    companion object {
        const val OFFSET = 0
        const val LIMIT = 50
    }

    override fun getRestaurantNearBy(
        lat: Float,
        lng: Float
    ): Single<List<RestaurantDataModel>> = apiService.fetchRestaurantNearBy(
        lat, lng,
        OFFSET,
        LIMIT
    )
        .map { response ->
            response.body() ?: throw RetrofitException.unexpectedError(RuntimeException("Unexpected server response"))
        }

    override fun getRestaurantDetail(restaurantId: Long): Single<RestaurantDetailDataModel> =
        apiService.fetchRestaurantDetail(restaurantId)
            .map { response ->
                response.body() ?: throw RetrofitException.unexpectedError(RuntimeException("Unexpected server response"))
            }
}