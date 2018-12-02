package com.ck.doordashproject.features.dashboard.presenter

import com.ck.doordashproject.base.modules.data.RestaurantDataModel

interface RestaurantViewHolderPresenter {
    fun onBind(restaurantDataModel: RestaurantDataModel)
    fun showRestaurantDetail()
}