package com.ck.doordashproject.features.dashboard.presenter

import androidx.lifecycle.DefaultLifecycleObserver

interface RestaurantListFragmentPresenter: DefaultLifecycleObserver {
    fun refresh()
}