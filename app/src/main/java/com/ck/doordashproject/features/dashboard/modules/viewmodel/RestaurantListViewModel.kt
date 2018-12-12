package com.ck.doordashproject.features.dashboard.modules.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ck.doordashproject.base.modules.data.RestaurantDataModel

class RestaurantListViewModel: ViewModel() {
    private val restaurantList = MutableLiveData<ArrayList<RestaurantDataModel>>()

    fun getRestaurants() : MutableLiveData<ArrayList<RestaurantDataModel>> {
        return  restaurantList
    }

    fun setRestaurants(newList: ArrayList<RestaurantDataModel>) {
        restaurantList.value = newList
    }
}