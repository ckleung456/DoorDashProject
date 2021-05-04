package com.ck.doordashproject.features.dashboard.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class LikedStatus: Parcelable {
    LIKED, UN_LIKED, NO_PREF
}