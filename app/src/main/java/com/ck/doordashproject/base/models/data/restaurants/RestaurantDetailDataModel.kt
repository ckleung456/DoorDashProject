package com.ck.doordashproject.base.models.data.restaurants

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RestaurantDetailDataModel (val id: Long, val phone_number: String, val delivery_fee: Long, val average_rating: Double,
                                      val status_type: String, val status: String, val yelp_rating: Double, val cover_img_url: String,
                                      val name: String, val description: String) : Parcelable