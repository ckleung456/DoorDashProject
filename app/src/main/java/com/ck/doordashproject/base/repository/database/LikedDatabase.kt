package com.ck.doordashproject.base.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ck.doordashproject.base.repository.database.entity.LikedDb

@Database(entities = [LikedDb::class], version = 1)
abstract class LikedDatabase : RoomDatabase() {
    abstract fun getLikedDataDao(): LikedDataDao
}