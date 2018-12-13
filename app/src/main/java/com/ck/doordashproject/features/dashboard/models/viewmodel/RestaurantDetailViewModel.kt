package com.ck.doordashproject.features.dashboard.models.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDetailDataModel

class RestaurantDetailViewModel: ViewModel() {
    private val restaurantDetailDataViewModel = MutableLiveData<RestaurantDetailDataModel>()

    fun observeRestaurantDetail(): MutableLiveData<RestaurantDetailDataModel> {
        return restaurantDetailDataViewModel
    }

    fun setRestaurantDetail(detailDataModel: RestaurantDetailDataModel) {
        restaurantDetailDataViewModel.value = detailDataModel
    }
}