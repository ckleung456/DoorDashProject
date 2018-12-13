package com.ck.doordashproject.base.network

import com.ck.doordashproject.base.models.data.restaurants.RestaurantDataModel
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDetailDataModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DoorDashAPIs {
    @GET(APIConstants.ENDPOINT_RESTAURANT_LIST)
    fun fetchRestaurantNearBy(
            @Query(APIConstants.ADDRESS_LAT) lat: Float,
            @Query(APIConstants.ADDRESS_LNG) lng: Float,
            @Query(APIConstants.OFFSET) offset: Int,
            @Query(APIConstants.LIMIT) limit: Int
    ): Observable<Response<List<RestaurantDataModel>>>

    @GET(APIConstants.ENDPOINT_RESTAURANT_DETAIL)
    fun fetchRestaurantDetail(@Path(APIConstants.RESTAURANT_ID) id: Long): Observable<Response<RestaurantDetailDataModel>>
}