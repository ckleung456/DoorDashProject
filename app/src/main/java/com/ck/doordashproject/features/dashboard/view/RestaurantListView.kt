package com.ck.doordashproject.features.dashboard.view

import com.ck.doordashproject.base.models.viewmodels.appnotification.AppNotificationViewModel
import com.ck.doordashproject.features.dashboard.models.viewmodel.RestaurantListViewModel
import java.lang.ref.WeakReference

interface RestaurantListView {
    fun getAppNotificationViewModel(): WeakReference<AppNotificationViewModel>
    fun getRestaurantListViewModel(): WeakReference<RestaurantListViewModel>
}