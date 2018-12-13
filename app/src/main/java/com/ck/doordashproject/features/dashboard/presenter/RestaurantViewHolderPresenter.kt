package com.ck.doordashproject.features.dashboard.presenter

import com.ck.doordashproject.base.models.data.restaurants.RestaurantDataModel

interface RestaurantViewHolderPresenter {
    fun onBind(restaurantDataModel: RestaurantDataModel)
    fun showRestaurantDetail()
}