package com.ck.doordashproject.features.dashboard.presenter

import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper
import com.ck.doordashproject.features.dashboard.ui.viewholders.RestaurantViewHolder

interface RestaurantAdatperPresenter {
    fun getItemCount(): Int
    fun setRestaurants(restaurantsList: ArrayList<RestaurantDataModelWrapper>)
    fun bindDataToHolder(holder: RestaurantViewHolder, position: Int, payloads: MutableList<Any>)
}