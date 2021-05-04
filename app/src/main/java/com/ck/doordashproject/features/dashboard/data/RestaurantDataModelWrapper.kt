package com.ck.doordashproject.features.dashboard.data

import android.os.Parcelable
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDataModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RestaurantDataModelWrapper(
    val restaurantData: RestaurantDataModel,
    var likeStatus: LikedStatus = LikedStatus.NO_PREF
): Parcelable