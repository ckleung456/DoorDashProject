package com.ck.doordashproject.base.repository.database.entity

import androidx.room.TypeConverter
import com.ck.doordashproject.features.dashboard.data.LikedStatus

class LikeStatusTypeConverter {
    @TypeConverter
    fun typeToInt(likedStatus: LikedStatus): Int {
        val result: Int =
            when (likedStatus) {
                LikedStatus.NO_PREF -> -1
                LikedStatus.LIKED -> 1
                LikedStatus.UN_LIKED -> 0
            }
        return result
    }

    @TypeConverter
    fun intToType(int: Int): LikedStatus {
        val result = when(int) {
            1 -> LikedStatus.LIKED
            0 -> LikedStatus.UN_LIKED
            else -> LikedStatus.NO_PREF
        }
        return result
    }
}