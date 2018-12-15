package com.ck.doordashproject.features.dashboard.models.repository.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ck.doordashproject.features.dashboard.data.LikedStatus

@Entity(tableName = "LikedDb")
@TypeConverters(LikeStatusTypeConverter::class)
data class LikedDb(@PrimaryKey var id: Long?, @ColumnInfo(name = "liked_status") var likedStatus: LikedStatus) {
    constructor(): this(null, LikedStatus.NO_PREF)
}