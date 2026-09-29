package com.example.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.VairalDatabase
import com.example.data.model.AdConfigEntity
import com.example.data.model.SavedVideoEntity
import com.example.data.model.UserEntity
import com.example.data.model.VideoEntity
import com.example.data.model.WatchHistoryEntity
import com.example.data.repository.AdRepository
import com.example.data.repository.AuthRepository
import com.example.data.repository.VideoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    TRENDING,
    SEARCH,
    ACCOUNT,
    VIDEO_PLAYER,
    ADMIN_LOGIN,
    ADMIN_DASHBOARD,
    ADMIN_VIDEOS,
    ADMIN_USERS,
    ADMIN_ADS
}

data class ShareOptions(
    val videoTitle: String,
    val videoUrl: String,
    val shareUrl: String
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = VairalDatabase.getDatabase(application)
    val videoRepo = VideoRepository(db.videoDao())
    val authRepo = AuthRepository(db.userDao(), db.userActivityDao())
    val adRepo = AdRepository(db.adConfigDao())

    // Navigation & Screen State
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _selectedVideoId = MutableStateFlow<String?>(null)
    val selectedVideoId: StateFlow<String?> = _selectedVideoId.asStateFlow()

    // Search query
    val searchQuery = MutableStateFlow("")

    // Category filter
    val selectedCategory = MutableStateFlow("All")

    // Ad gating state for current playback session: Map of videoId -> isUnlocked
    private val _unlockedVideos = MutableStateFlow<Set<String>>(emptySet())
    val unlockedVideos: StateFlow<Set<String>> = _unlockedVideos.asStateFlow()

    // Share dialog state
    private val _shareDialogInfo = MutableStateFlow<ShareOptions?>(null)
    val shareDialogInfo: StateFlow<ShareOptions?> = _shareDialogInfo.asStateFlow()

    // Notification / Toast message
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    // Data streams
    val publishedVideos: StateFlow<List<VideoEntity>> = videoRepo.allPublishedVideos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val adminVideos: StateFlow<List<VideoEntity>> = videoRepo.allAdminVideos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val trendingVideos: StateFlow<List<VideoEntity>> = videoRepo.trendingVideos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val adConfig: StateFlow<AdConfigEntity> = adRepo.currentConfig.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AdConfigEntity()
    )

    val currentUser: StateFlow<UserEntity?> = authRepo.currentUser

    val allUsers: StateFlow<List<UserEntity>> = authRepo.allUsers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val totalUsers: StateFlow<Int> = authRepo.totalUsers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val activeUsers: StateFlow<Int> = authRepo.activeUsers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val totalVideos: StateFlow<Int> = videoRepo.totalVideos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val totalViews: StateFlow<Long> = videoRepo.totalViews.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val totalShares: StateFlow<Long> = videoRepo.totalShares.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val watchHistory: StateFlow<List<WatchHistoryEntity>> = authRepo.watchHistory.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val savedVideos: StateFlow<List<SavedVideoEntity>> = authRepo.savedVideos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Filtered videos based on category and search
    val filteredVideos: StateFlow<List<VideoEntity>> = combine(
        publishedVideos,
        searchQuery,
        selectedCategory
    ) { videos, query, category ->
        var list = videos
        if (category != "All") {
            list = list.filter { it.category.equals(category, ignoreCase = true) }
        }
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                it.description.lowercase().contains(q) ||
                it.tags.lowercase().contains(q)
            }
        }
        list
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            videoRepo.seedInitialVideosIfEmpty()
            adRepo.seedInitialConfigIfEmpty()
            authRepo.seedInitialUsersAndAdminIfEmpty()
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun openVideo(videoId: String) {
        _selectedVideoId.value = videoId
        _currentScreen.value = AppScreen.VIDEO_PLAYER
        viewModelScope.launch {
            videoRepo.recordView(videoId)
            authRepo.recordWatch(videoId)
        }
    }

    fun unlockVideoAd(videoId: String, context: Context) {
        viewModelScope.launch {
            val config = adRepo.getAdConfigSync()
            if (config.isEnabled && config.adUrl.isNotBlank()) {
                try {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(config.adUrl)).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(browserIntent)
                } catch (e: Exception) {
                    // Fallback
                }
            }
            // Unlock video playback for user
            _unlockedVideos.value = _unlockedVideos.value + videoId
            _userMessage.value = "Advertisement verified. Video unlocked!"
        }
    }

    fun isVideoUnlocked(videoId: String): Boolean {
        val config = adConfig.value
        if (!config.isEnabled) return true
        return _unlockedVideos.value.contains(videoId)
    }

    fun openShareDialog(video: VideoEntity) {
        val shareUrl = "https://vairal6t9.web.app/video/${video.id}"
        _shareDialogInfo.value = ShareOptions(
            videoTitle = video.title,
            videoUrl = video.videoUrl,
            shareUrl = shareUrl
        )
    }

    fun closeShareDialog() {
        _shareDialogInfo.value = null
    }

    fun onSharePerformed(videoId: String) {
        viewModelScope.launch {
            videoRepo.recordShare(videoId)
        }
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    fun showMessage(msg: String) {
        _userMessage.value = msg
    }

    // Admin Actions
    fun uploadVideo(
        title: String,
        description: String,
        thumbnailUrl: String,
        videoUrl: String,
        category: String,
        tags: String,
        duration: String,
        isTrending: Boolean,
        published: Boolean
    ) {
        viewModelScope.launch {
            val newId = videoRepo.addVideo(
                title = title,
                description = description,
                thumbnailUrl = thumbnailUrl,
                videoUrl = videoUrl,
                category = category,
                tags = tags,
                duration = duration,
                isTrending = isTrending,
                published = published
            )
            _userMessage.value = "Video published successfully! ID: $newId"
        }
    }

    fun deleteVideo(videoId: String) {
        viewModelScope.launch {
            videoRepo.deleteVideo(videoId)
            _userMessage.value = "Video deleted."
        }
    }

    fun togglePublish(videoId: String, currentStatus: Boolean) {
        viewModelScope.launch {
            videoRepo.togglePublishStatus(videoId, currentStatus)
            _userMessage.value = if (!currentStatus) "Video published." else "Video unpublished."
        }
    }

    fun saveAdSettings(
        adUrl: String,
        isEnabled: Boolean,
        interstitialEnabled: Boolean,
        placement: String,
        admobAppId: String,
        admobBannerId: String,
        admobInterstitialId: String,
        admobRewardedId: String
    ) {
        viewModelScope.launch {
            adRepo.updateAdConfig(
                adUrl = adUrl,
                isEnabled = isEnabled,
                interstitialEnabled = interstitialEnabled,
                placement = placement,
                admobAppId = admobAppId,
                admobBannerId = admobBannerId,
                admobInterstitialId = admobInterstitialId,
                admobRewardedId = admobRewardedId
            )
            _userMessage.value = "Advertisement settings updated successfully!"
        }
    }

    fun toggleUserStatus(userId: String, currentStatus: String) {
        viewModelScope.launch {
            authRepo.toggleUserStatus(userId, currentStatus)
            _userMessage.value = "User status updated."
        }
    }

    fun deleteUser(userId: String) {
        viewModelScope.launch {
            authRepo.deleteUser(userId)
            _userMessage.value = "User removed."
        }
    }
}
