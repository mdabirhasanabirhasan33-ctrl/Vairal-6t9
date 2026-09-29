package com.example.data.repository

import com.example.data.local.VideoDao
import com.example.data.model.VideoEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class VideoRepository(private val videoDao: VideoDao) {

    val allPublishedVideos: Flow<List<VideoEntity>> = videoDao.getAllPublishedVideos()
    val allAdminVideos: Flow<List<VideoEntity>> = videoDao.getAllVideosAdmin()
    val trendingVideos: Flow<List<VideoEntity>> = videoDao.getTrendingVideos()
    val totalVideos: Flow<Int> = videoDao.getTotalVideoCount()
    val totalViews: Flow<Long> = videoDao.getTotalViewCount()
    val totalShares: Flow<Long> = videoDao.getTotalShareCount()

    fun getVideoById(id: String): Flow<VideoEntity?> = videoDao.getVideoById(id)
    suspend fun getVideoByIdSync(id: String): VideoEntity? = videoDao.getVideoByIdSync(id)

    fun searchVideos(query: String): Flow<List<VideoEntity>> = videoDao.searchVideos(query)
    fun getVideosByCategory(category: String): Flow<List<VideoEntity>> = videoDao.getVideosByCategory(category)

    suspend fun addVideo(
        title: String,
        description: String,
        thumbnailUrl: String,
        videoUrl: String,
        category: String,
        tags: String,
        duration: String = "1:00",
        isTrending: Boolean = false,
        published: Boolean = true
    ): String {
        val id = UUID.randomUUID().toString().substring(0, 8)
        val video = VideoEntity(
            id = id,
            title = title.trim(),
            description = description.trim(),
            thumbnailUrl = thumbnailUrl.trim().ifEmpty { "https://images.unsplash.com/photo-1536240478700-b869070f9279?w=800&auto=format&fit=crop&q=80" },
            videoUrl = videoUrl.trim().ifEmpty { "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4" },
            category = category,
            tags = tags.trim(),
            duration = duration,
            published = published,
            isTrending = isTrending,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        videoDao.insertVideo(video)
        return id
    }

    suspend fun updateVideo(video: VideoEntity) {
        videoDao.updateVideo(video.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun deleteVideo(id: String) {
        videoDao.deleteVideoById(id)
    }

    suspend fun togglePublishStatus(id: String, currentStatus: Boolean) {
        videoDao.setPublishStatus(id, !currentStatus)
    }

    suspend fun recordView(id: String) {
        videoDao.incrementViews(id)
    }

    suspend fun recordShare(id: String) {
        videoDao.incrementShares(id)
    }

    suspend fun seedInitialVideosIfEmpty() {
        val count = videoDao.getVideoByIdSync("v-6t9-01")
        if (count == null) {
            val initialVideos = listOf(
                VideoEntity(
                    id = "v-6t9-01",
                    title = "VAIRAL 6T9 Premiere: The Next Era of Speed & Beat",
                    description = "Exclusive high-energy cinematic showcase featuring ultra-fast drifting, cyberpunk lights, and bass drop beats. Powered by VAIRAL 6T9.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                    category = "Viral",
                    tags = "viral, trending, premiere, 6t9",
                    duration = "0:15",
                    views = 124800,
                    shares = 38400,
                    isTrending = true,
                    published = true
                ),
                VideoEntity(
                    id = "v-6t9-02",
                    title = "Insane Freestyle Acrobatics in Neo City Center",
                    description = "Watch gravity-defying parkour stunts performed atop towering skyscraper rooftops with zero safety nets. Do not attempt this at home!",
                    thumbnailUrl = "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?w=800&auto=format&fit=crop&q=80",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                    category = "Action",
                    tags = "parkour, stunts, extreme, action",
                    duration = "0:15",
                    views = 89200,
                    shares = 19400,
                    isTrending = true,
                    published = true
                ),
                VideoEntity(
                    id = "v-6t9-03",
                    title = "Deep Space Pulse: Aurora Symphony 8K",
                    description = "Hyperlapse of cosmic solar flares dancing over the Arctic circle. Mesmerizing glowing greens and electric magentas.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1531306728370-e2ebd9d7bb99?w=800&auto=format&fit=crop&q=80",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                    category = "Nature",
                    tags = "space, aurora, 8k, relaxing",
                    duration = "1:00",
                    views = 45100,
                    shares = 8120,
                    isTrending = false,
                    published = true
                ),
                VideoEntity(
                    id = "v-6t9-04",
                    title = "AI Cyberpunk Soundscape Live Mixing Session",
                    description = "Analog modular synths colliding with neural network audio stems in a live neon underground warehouse.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=800&auto=format&fit=crop&q=80",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4",
                    category = "Music",
                    tags = "music, live, synth, edm",
                    duration = "0:15",
                    views = 67300,
                    shares = 14200,
                    isTrending = true,
                    published = true
                ),
                VideoEntity(
                    id = "v-6t9-05",
                    title = "Street Food Master: Flame Wok Secrets Unlocked",
                    description = "The sizzling art of master wok chefs cooking noodles in bursts of roaring dragon fire in midnight alleyways.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=800&auto=format&fit=crop&q=80",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
                    category = "Lifestyle",
                    tags = "food, chef, streetfood, delicious",
                    duration = "0:15",
                    views = 31200,
                    shares = 6500,
                    isTrending = false,
                    published = true
                )
            )
            videoDao.insertVideos(initialVideos)
        }
    }
}
