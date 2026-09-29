package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val category: String,
    val tags: String = "",
    val duration: String = "0:30",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val published: Boolean = true,
    val views: Long = 0,
    val shares: Long = 0,
    val isTrending: Boolean = false
)
