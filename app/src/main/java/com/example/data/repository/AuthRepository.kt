package com.example.data.repository

import com.example.data.local.UserActivityDao
import com.example.data.local.UserDao
import com.example.data.model.SavedVideoEntity
import com.example.data.model.UserEntity
import com.example.data.model.WatchHistoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class AuthRepository(
    private val userDao: UserDao,
    private val userActivityDao: UserActivityDao
) {
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    val allUsers: Flow<List<UserEntity>> = userDao.getAllUsers()
    val totalUsers: Flow<Int> = userDao.getTotalUserCount()
    val activeUsers: Flow<Int> = userDao.getActiveUserCount()

    val watchHistory: Flow<List<WatchHistoryEntity>> = userActivityDao.getWatchHistory()
    val savedVideos: Flow<List<SavedVideoEntity>> = userActivityDao.getSavedVideos()

    suspend fun register(email: String, displayName: String): Result<UserEntity> {
        val trimmedEmail = email.trim().lowercase()
        if (trimmedEmail.isEmpty() || !trimmedEmail.contains("@")) {
            return Result.failure(IllegalArgumentException("Please enter a valid email address"))
        }

        val existing = userDao.getUserByEmail(trimmedEmail)
        if (existing != null) {
            return Result.failure(IllegalStateException("An account with this email already exists"))
        }

        val newUser = UserEntity(
            id = UUID.randomUUID().toString().substring(0, 8),
            email = trimmedEmail,
            displayName = displayName.trim().ifEmpty { trimmedEmail.substringBefore("@") },
            role = "USER",
            status = "ACTIVE"
        )
        userDao.insertUser(newUser)
        _currentUser.value = newUser
        return Result.success(newUser)
    }

    suspend fun login(email: String): Result<UserEntity> {
        val trimmedEmail = email.trim().lowercase()
        val user = userDao.getUserByEmail(trimmedEmail)
            ?: return Result.failure(IllegalArgumentException("No account found with this email"))

        if (user.status == "DISABLED") {
            return Result.failure(IllegalStateException("This account has been disabled by an administrator"))
        }

        _currentUser.value = user
        return Result.success(user)
    }

    suspend fun adminLogin(email: String): Result<UserEntity> {
        val trimmedEmail = email.trim().lowercase()
        val user = userDao.getUserByEmail(trimmedEmail)
            ?: return Result.failure(IllegalArgumentException("Admin account not found"))

        if (user.role != "ADMIN") {
            return Result.failure(SecurityException("Access denied: Not an administrator account"))
        }

        if (user.status == "DISABLED") {
            return Result.failure(IllegalStateException("Admin account has been disabled"))
        }

        _currentUser.value = user
        return Result.success(user)
    }

    fun logout() {
        _currentUser.value = null
    }

    suspend fun toggleUserStatus(userId: String, currentStatus: String) {
        val newStatus = if (currentStatus == "ACTIVE") "DISABLED" else "ACTIVE"
        userDao.updateUserStatus(userId, newStatus)
    }

    suspend fun deleteUser(userId: String) {
        userDao.deleteUserById(userId)
        if (_currentUser.value?.id == userId) {
            _currentUser.value = null
        }
    }

    suspend fun recordWatch(videoId: String) {
        userActivityDao.recordWatch(WatchHistoryEntity(videoId = videoId))
    }

    suspend fun clearHistory() {
        userActivityDao.clearWatchHistory()
    }

    fun isSaved(videoId: String): Flow<Boolean> = userActivityDao.isSaved(videoId)

    suspend fun toggleSave(videoId: String, isCurrentlySaved: Boolean) {
        if (isCurrentlySaved) {
            userActivityDao.removeSavedVideo(videoId)
        } else {
            userActivityDao.saveVideo(SavedVideoEntity(videoId = videoId))
        }
    }

    suspend fun seedInitialUsersAndAdminIfEmpty() {
        val admin = userDao.getFirstAdmin()
        if (admin == null) {
            val defaultAdmin = UserEntity(
                id = "admin-root",
                email = "admin@vairal6t9.com",
                displayName = "VAIRAL 6T9 Chief Admin",
                role = "ADMIN",
                status = "ACTIVE"
            )
            val demoUser = UserEntity(
                id = "user-demo",
                email = "creator@vairal6t9.com",
                displayName = "NeonCreator",
                role = "USER",
                status = "ACTIVE"
            )
            userDao.insertUser(defaultAdmin)
            userDao.insertUser(demoUser)
        }
    }
}
