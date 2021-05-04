package com.ck.doordashproject.features.dashboard.models.data

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class RestaurantDetailViewDataModel(
    val logoDrawable: @RawValue Drawable?,
    val logoBitmap: Bitmap?,
    val name: String,
    val status: String,
    val phoneNumber: String,
    val description: String,
    val deliveryFee: Double,
    val yelpRating: Double,
    val averageRating: Double
) : Parcelable