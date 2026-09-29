package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ad_configs")
data class AdConfigEntity(
    @PrimaryKey val id: Int = 1,
    val adUrl: String = "https://www.profitableratecpmnetwork.com/cv45kw4t?key=2da90e2e73d7f0ed30da1d215c04c66e",
    val isEnabled: Boolean = true,
    val interstitialEnabled: Boolean = true,
    val placement: String = "CLICK_TO_WATCH_GATE", // Options: CLICK_TO_WATCH_GATE, INTERSTITIAL_PRE_ROLL, BANNER
    val frequencyMinutes: Int = 0, // 0 = every watch
    val admobAppId: String = "ca-app-pub-3940256099942544~3347511713",
    val admobBannerId: String = "ca-app-pub-3940256099942544/6300978111",
    val admobInterstitialId: String = "ca-app-pub-3940256099942544/1033173712",
    val admobRewardedId: String = "ca-app-pub-3940256099942544/5224354917",
    val updatedAt: Long = System.currentTimeMillis()
)
