package com.ck.doordashproject.features.dashboard.view

import com.ck.doordashproject.features.dashboard.modules.viewmodel.RestaurantListViewModel
import java.lang.ref.WeakReference

interface RestaurantListView {
    fun getRestaurantListViewModel(): WeakReference<RestaurantListViewModel?>
}