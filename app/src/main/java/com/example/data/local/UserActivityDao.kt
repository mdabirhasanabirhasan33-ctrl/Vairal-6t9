package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.SavedVideoEntity
import com.example.data.model.WatchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserActivityDao {
    @Query("SELECT * FROM watch_history ORDER BY watchedAt DESC")
    fun getWatchHistory(): Flow<List<WatchHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun recordWatch(entry: WatchHistoryEntity)

    @Query("DELETE FROM watch_history")
    suspend fun clearWatchHistory()

    @Query("SELECT * FROM saved_videos ORDER BY savedAt DESC")
    fun getSavedVideos(): Flow<List<SavedVideoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveVideo(entry: SavedVideoEntity)

    @Query("DELETE FROM saved_videos WHERE videoId = :videoId")
    suspend fun removeSavedVideo(videoId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_videos WHERE videoId = :videoId)")
    fun isSaved(videoId: String): Flow<Boolean>
}
