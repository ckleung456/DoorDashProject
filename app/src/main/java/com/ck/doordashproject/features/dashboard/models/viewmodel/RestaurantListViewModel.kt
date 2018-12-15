package com.ck.doordashproject.features.dashboard.models.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper

class RestaurantListViewModel : ViewModel() {
    private val restaurantList = MutableLiveData<ArrayList<RestaurantDataModelWrapper>>()

    fun observeRestaurantsList(): MutableLiveData<ArrayList<RestaurantDataModelWrapper>> {
        return restaurantList
    }

    fun setRestaurants(newList: ArrayList<RestaurantDataModelWrapper>) {
        restaurantList.postValue(newList)
    }
}