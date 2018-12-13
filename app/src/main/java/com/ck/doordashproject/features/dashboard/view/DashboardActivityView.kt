package com.ck.doordashproject.features.dashboard.view

interface DashboardActivityView {
    fun setTitle(title: String)
    fun launchRestaurantsList()
    fun launchRestaurantDetail()
}