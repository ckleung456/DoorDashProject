package com.ck.doordashproject.features.dashboard.models.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper
import com.ck.doordashproject.features.dashboard.models.data.RestaurantDetailViewDataModel

class RestaurantViewModel: ViewModel() {
    private val showPageViewModel = MutableLiveData<String>()
    private val restaurantList = MutableLiveData<ArrayList<RestaurantDataModelWrapper>>()
    private val restaurantDetailDataViewModel = MutableLiveData<RestaurantDetailViewDataModel>()

    fun setShowPage(pageTag: String) = showPageViewModel.postValue(pageTag)

    fun observeShowPage() = showPageViewModel

    fun observeRestaurantsList(): MutableLiveData<ArrayList<RestaurantDataModelWrapper>> {
        return restaurantList
    }

    fun setRestaurants(newList: ArrayList<RestaurantDataModelWrapper>) {
        restaurantList.postValue(newList)
    }

    fun observeRestaurantDetail(): MutableLiveData<RestaurantDetailViewDataModel> {
        return restaurantDetailDataViewModel
    }

    fun setRestaurantDetail(detailDataModel: RestaurantDetailViewDataModel) {
        restaurantDetailDataViewModel.value = detailDataModel
    }
}