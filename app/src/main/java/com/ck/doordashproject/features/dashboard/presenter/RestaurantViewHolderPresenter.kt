package com.ck.doordashproject.features.dashboard.presenter

import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper

interface RestaurantViewHolderPresenter {
    fun onBind(restaurantDataModel: RestaurantDataModelWrapper)
    fun showRestaurantDetail()
    fun performLikeOption()
}