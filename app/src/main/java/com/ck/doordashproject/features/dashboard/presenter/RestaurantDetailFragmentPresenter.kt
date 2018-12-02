package com.ck.doordashproject.features.dashboard.presenter

import androidx.lifecycle.DefaultLifecycleObserver
import com.ck.doordashproject.base.modules.data.RestaurantDetailDataModel

interface RestaurantDetailFragmentPresenter: DefaultLifecycleObserver {
    fun setDetail(detailDataModel: RestaurantDetailDataModel)
}