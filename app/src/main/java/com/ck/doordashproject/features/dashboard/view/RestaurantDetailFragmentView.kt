package com.ck.doordashproject.features.dashboard.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

interface RestaurantDetailFragmentView {
    fun setRestaurantLogo(drawable: Drawable)
    fun setRestaurantLogo(bitmap: Bitmap)
    fun setRestaurantName(name: String)
    fun setRestaurantStatus(status: String)
    fun setRestaurantPhoneNumber(phoneNumber: String)
    fun setRestaurantDescription(description: String)
    fun setRestaurantDeliveryFee(deliveryFee: Double)
    fun setRestaurantYelpRating(rating: Double)
    fun setRestaurantAverageRating(rating: Double)
}