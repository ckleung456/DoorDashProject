package com.ck.doordashproject.features.dashboard.models.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDataModel

class RestaurantListViewModel : ViewModel() {
    private val restaurantList = MutableLiveData<ArrayList<RestaurantDataModel>>()

    fun observeRestaurantsList(): MutableLiveData<ArrayList<RestaurantDataModel>> {
        return restaurantList
    }

    fun setRestaurants(newList: ArrayList<RestaurantDataModel>) {
        restaurantList.value = newList
    }
}