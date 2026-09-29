package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.AdConfigEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AdConfigDao {
    @Query("SELECT * FROM ad_configs WHERE id = 1 LIMIT 1")
    fun getAdConfig(): Flow<AdConfigEntity?>

    @Query("SELECT * FROM ad_configs WHERE id = 1 LIMIT 1")
    suspend fun getAdConfigSync(): AdConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAdConfig(config: AdConfigEntity)
}
