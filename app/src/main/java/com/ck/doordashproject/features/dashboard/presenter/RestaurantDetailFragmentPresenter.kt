package com.ck.doordashproject.features.dashboard.presenter

import com.ck.doordashproject.base.models.data.restaurants.RestaurantDetailDataModel

interface RestaurantDetailFragmentPresenter {
    fun setDetail(detailDataModel: RestaurantDetailDataModel)
}