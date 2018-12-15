package com.ck.doordashproject.features.dashboard.models.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.ck.doordashproject.features.dashboard.models.repository.database.entity.LikedDb

@Dao
interface LikedDataDao {
    @Query("SELECT * FROM LikedDb")
    fun getAll(): List<LikedDb>

    @Query("SELECT * FROM LikedDb WHERE id = :id")
    fun getById(id: Long): LikedDb?

    @Insert(onConflict = REPLACE)
    fun insert(likedDb: LikedDb)

    @Query("DELETE FROM LikedDb")
    fun deleteAll()
}