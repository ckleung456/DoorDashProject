package com.ck.doordashproject.features.dashboard.models.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ck.doordashproject.features.dashboard.models.repository.database.entity.LikedDb

@Database(entities = [LikedDb::class], version = 1)
abstract class LikedDatabase : RoomDatabase() {
    abstract fun getDao(): LikedDataDao

    companion object {
        private var INSTANCE: LikedDatabase? = null

        fun getInstance(context: Context): LikedDatabase? {
            if (INSTANCE == null) {
                synchronized(LikedDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LikedDatabase::class.java, "liked.db"
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}