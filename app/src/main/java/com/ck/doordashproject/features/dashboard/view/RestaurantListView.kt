package com.ck.doordashproject.features.dashboard.view

import com.ck.doordashproject.base.modules.data.RestaurantDataModel

interface RestaurantListView {
    fun setRestaurants(restaurants: ArrayList<RestaurantDataModel>)
    fun onRefreshDone()
}