package com.ck.doordashproject.base.models.data.restaurants

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RestaurantDetailDataModel(
    val id: Long = Long.MIN_VALUE,
    val phone_number: String? = null,
    val delivery_fee: Long? = Long.MIN_VALUE,
    val average_rating: Double? = Double.MIN_VALUE,
    val status_type: String? = null,
    val status: String? = null,
    val yelp_rating: Double? = Double.MIN_VALUE,
    val cover_img_url: String? = null,
    val name: String? = null,
    val description: String? = null
) : Parcelable