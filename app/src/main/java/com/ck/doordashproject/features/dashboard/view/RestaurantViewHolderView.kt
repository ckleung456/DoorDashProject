package com.ck.doordashproject.features.dashboard.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

interface RestaurantViewHolderView {
    fun setRestaurantName(name: String)
    fun setRestaurantSubTitle(subTitle: String)
    fun setRestaurantLogo(bitmap: Bitmap)
    fun setRestaurantLogo(drawable: Drawable)
    fun setRestaurantStatus(status: String)
}