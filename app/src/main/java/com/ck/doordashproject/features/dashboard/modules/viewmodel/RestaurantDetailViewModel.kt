package com.ck.doordashproject.features.dashboard.modules.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ck.doordashproject.base.modules.data.RestaurantDetailDataModel

class RestaurantDetailViewModel: ViewModel() {
    private val restaurantDetailDataViewModel = MutableLiveData<RestaurantDetailDataModel>()

    fun getRestaurantDetail(): MutableLiveData<RestaurantDetailDataModel> {
        return restaurantDetailDataViewModel
    }

    fun setRestaurantDetail(detailDataModel: RestaurantDetailDataModel) {
        restaurantDetailDataViewModel.value = detailDataModel
    }
}