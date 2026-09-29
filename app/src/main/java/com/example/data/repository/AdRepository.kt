package com.example.data.repository

import com.example.data.local.AdConfigDao
import com.example.data.model.AdConfigEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AdRepository(private val adConfigDao: AdConfigDao) {

    val currentConfig: Flow<AdConfigEntity> = adConfigDao.getAdConfig().map { config ->
        config ?: AdConfigEntity()
    }

    suspend fun getAdConfigSync(): AdConfigEntity {
        return adConfigDao.getAdConfigSync() ?: AdConfigEntity().also {
            adConfigDao.saveAdConfig(it)
        }
    }

    suspend fun updateAdConfig(
        adUrl: String,
        isEnabled: Boolean,
        interstitialEnabled: Boolean,
        placement: String,
        admobAppId: String,
        admobBannerId: String,
        admobInterstitialId: String,
        admobRewardedId: String
    ) {
        val updated = AdConfigEntity(
            id = 1,
            adUrl = adUrl.trim().ifEmpty { "https://www.profitableratecpmnetwork.com/cv45kw4t?key=2da90e2e73d7f0ed30da1d215c04c66e" },
            isEnabled = isEnabled,
            interstitialEnabled = interstitialEnabled,
            placement = placement,
            admobAppId = admobAppId.trim(),
            admobBannerId = admobBannerId.trim(),
            admobInterstitialId = admobInterstitialId.trim(),
            admobRewardedId = admobRewardedId.trim(),
            updatedAt = System.currentTimeMillis()
        )
        adConfigDao.saveAdConfig(updated)
    }

    suspend fun seedInitialConfigIfEmpty() {
        val existing = adConfigDao.getAdConfigSync()
        if (existing == null) {
            adConfigDao.saveAdConfig(AdConfigEntity())
        }
    }
}
