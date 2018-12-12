package com.ck.doordashproject.features.dashboard.presenter

import com.ck.doordashproject.base.modules.data.RestaurantDetailDataModel

interface RestaurantDetailFragmentPresenter {
    fun setDetail(detailDataModel: RestaurantDetailDataModel)
}