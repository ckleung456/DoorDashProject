package com.ck.doordashproject.base.modules.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RestaurantDataModel(val id: Long, val name: String, val description: String,
                               val cover_img_url: String, val status: String, val delivery_fee: String, val status_type: String): Parcelable