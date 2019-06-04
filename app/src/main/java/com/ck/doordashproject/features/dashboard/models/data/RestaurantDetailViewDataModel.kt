package com.ck.doordashproject.features.dashboard.models.data

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

data class RestaurantDetailViewDataModel (val logoDrawable: Drawable?, val logoBitmap: Bitmap?, val name: String,
                                          val status: String, val phoneNumber: String,
                                          val description: String, val deliveryFee: Double,
                                          val yelpRating: Double, val averageRating: Double)