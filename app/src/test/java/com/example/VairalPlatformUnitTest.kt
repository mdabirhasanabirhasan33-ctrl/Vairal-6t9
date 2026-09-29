package com.example

import com.example.data.model.AdConfigEntity
import com.example.data.model.VideoEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class VairalPlatformUnitTest {

    @Test
    fun `test default ad configuration`() {
        val config = AdConfigEntity()
        assertEquals(
            "https://www.profitableratecpmnetwork.com/cv45kw4t?key=2da90e2e73d7f0ed30da1d215c04c66e",
            config.adUrl
        )
        assertTrue(config.isEnabled)
        assertTrue(config.interstitialEnabled)
        assertEquals("CLICK_TO_WATCH_GATE", config.placement)
        assertNotNull(config.admobAppId)
        assertNotNull(config.admobBannerId)
        assertNotNull(config.admobInterstitialId)
        assertNotNull(config.admobRewardedId)
    }

    @Test
    fun `test video entity properties`() {
        val video = VideoEntity(
            id = "test-01",
            title = "Viral Dance 6T9",
            description = "High energy viral music",
            thumbnailUrl = "https://example.com/thumb.jpg",
            videoUrl = "https://example.com/video.mp4",
            category = "Viral",
            published = true,
            views = 500,
            shares = 120
        )
        assertEquals("test-01", video.id)
        assertTrue(video.published)
        assertEquals(500L, video.views)
        assertEquals(120L, video.shares)
    }
}
