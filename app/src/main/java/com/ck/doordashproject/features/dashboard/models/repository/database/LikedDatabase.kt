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
        private val LOCK = Unit

        fun getInstance(context: Context): LikedDatabase {
            synchronized(LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LikedDatabase::class.java, "liked.db"
                    ).build()
                }
                return INSTANCE!!
            }
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}