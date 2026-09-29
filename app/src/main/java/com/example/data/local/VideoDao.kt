package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Query("SELECT * FROM videos WHERE published = 1 ORDER BY createdAt DESC")
    fun getAllPublishedVideos(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos ORDER BY createdAt DESC")
    fun getAllVideosAdmin(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE published = 1 AND isTrending = 1 ORDER BY views DESC")
    fun getTrendingVideos(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE id = :id LIMIT 1")
    fun getVideoById(id: String): Flow<VideoEntity?>

    @Query("SELECT * FROM videos WHERE id = :id LIMIT 1")
    suspend fun getVideoByIdSync(id: String): VideoEntity?

    @Query("SELECT * FROM videos WHERE published = 1 AND (title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%') ORDER BY createdAt DESC")
    fun searchVideos(query: String): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE published = 1 AND category = :category ORDER BY createdAt DESC")
    fun getVideosByCategory(category: String): Flow<List<VideoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<VideoEntity>)

    @Update
    suspend fun updateVideo(video: VideoEntity)

    @Query("DELETE FROM videos WHERE id = :id")
    suspend fun deleteVideoById(id: String)

    @Query("UPDATE videos SET views = views + 1 WHERE id = :id")
    suspend fun incrementViews(id: String)

    @Query("UPDATE videos SET shares = shares + 1 WHERE id = :id")
    suspend fun incrementShares(id: String)

    @Query("UPDATE videos SET published = :published, updatedAt = :updatedAt WHERE id = :id")
    suspend fun setPublishStatus(id: String, published: Boolean, updatedAt: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM videos")
    fun getTotalVideoCount(): Flow<Int>

    @Query("SELECT COALESCE(SUM(views), 0) FROM videos")
    fun getTotalViewCount(): Flow<Long>

    @Query("SELECT COALESCE(SUM(shares), 0) FROM videos")
    fun getTotalShareCount(): Flow<Long>
}
