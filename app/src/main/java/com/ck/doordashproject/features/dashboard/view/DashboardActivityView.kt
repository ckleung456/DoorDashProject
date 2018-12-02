package com.ck.doordashproject.features.dashboard.view

import com.ck.doordashproject.base.modules.data.RestaurantDetailDataModel

interface DashboardActivityView {
    fun setTitle(title: String)
    fun launchRestaurantsList()
    fun launchRestaurantDetail(detailData: RestaurantDetailDataModel)
}