package com.ck.doordashproject.features.dashboard.presenter

import com.ck.doordashproject.base.models.data.restaurants.RestaurantDataModel
import com.ck.doordashproject.features.dashboard.ui.viewholders.RestaurantViewHolder

interface RestaurantAdatperPresenter {
    fun getItemCount(): Int
    fun setRestaurants(restaurantsList: ArrayList<RestaurantDataModel>)
    fun bindDataToHolder(holder: RestaurantViewHolder, position: Int, payloads: MutableList<Any>)
}