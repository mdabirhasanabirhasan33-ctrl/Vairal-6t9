package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VideoEntity
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.VideoCard
import com.example.ui.components.VideoPlayerView
import com.example.ui.components.formatNumber
import com.example.ui.theme.VairalCard
import com.example.ui.theme.VairalDarkBg
import com.example.ui.theme.VairalRed
import com.example.ui.theme.VairalSurfaceVariant
import com.example.ui.theme.VairalTextPrimary
import com.example.ui.theme.VairalTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoPlayerScreen(
    videoId: String,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    BackHandler { onBack() }

    val videos by viewModel.publishedVideos.collectAsState()
    val video = videos.find { it.id == videoId }
    val adConfig by viewModel.adConfig.collectAsState()
    val isUnlocked = viewModel.isVideoUnlocked(videoId)

    var isLiked by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }
    var isDescExpanded by remember { mutableStateOf(false) }

    if (video == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(VairalDarkBg),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Video not found or has been unpublished.", color = VairalTextSecondary)
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = VairalRed)) {
                    Text("Return to Feed")
                }
            }
        }
        return
    }

    val shareUrl = "https://vairal6t9.web.app/video/${video.id}"
    val relatedVideos = videos.filter { it.id != video.id }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(VairalDarkBg),
        contentPadding = PaddingValues(bottom = 60.dp)
    ) {
        // VIDEO PLAYER COMPONENT
        item {
            VideoPlayerView(
                videoUrl = video.videoUrl,
                thumbnailUrl = video.thumbnailUrl,
                isUnlocked = isUnlocked,
                onUnlockAdClick = {
                    viewModel.unlockVideoAd(videoId, context)
                }
            )
        }

        // VIDEO TITLE & META
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Text(
                    text = video.title,
                    color = VairalTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${formatNumber(video.views)} views • ${formatDate(video.createdAt)}",
                        color = VairalTextSecondary,
                        fontSize = 12.sp
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(VairalSurfaceVariant)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = video.category,
                            color = VairalRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // INTERACTIVE ACTION BUTTONS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Like
                    ActionButton(
                        icon = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        label = if (isLiked) "Liked" else "Like",
                        tint = if (isLiked) VairalRed else VairalTextPrimary,
                        onClick = { isLiked = !isLiked }
                    )

                    // Save
                    ActionButton(
                        icon = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        label = if (isSaved) "Saved" else "Save",
                        tint = if (isSaved) VairalRed else VairalTextPrimary,
                        onClick = { isSaved = !isSaved }
                    )

                    // Copy Link
                    ActionButton(
                        icon = Icons.Default.ContentCopy,
                        label = "Copy Link",
                        tint = VairalTextPrimary,
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("VAIRAL 6T9 Link", shareUrl)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Video link copied!", Toast.LENGTH_SHORT).show()
                            viewModel.onSharePerformed(video.id)
                        }
                    )

                    // Share
                    ActionButton(
                        icon = Icons.Default.Share,
                        label = "Share",
                        tint = VairalRed,
                        onClick = {
                            viewModel.openShareDialog(video)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // DESCRIPTION & TAGS CARD
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = VairalCard),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isDescExpanded = !isDescExpanded }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = video.description,
                            color = VairalTextPrimary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            maxLines = if (isDescExpanded) Int.MAX_VALUE else 2
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isDescExpanded) "Show less" else "...more",
                            color = VairalRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        if (video.tags.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                video.tags.split(",").forEach { tag ->
                                    val trimmed = tag.trim()
                                    if (trimmed.isNotEmpty()) {
                                        SuggestionChip(
                                            onClick = { },
                                            label = { Text("#$trimmed", fontSize = 11.sp) },
                                            colors = SuggestionChipDefaults.suggestionChipColors(
                                                containerColor = VairalSurfaceVariant,
                                                labelColor = VairalTextSecondary
                                            ),
                                            border = null
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ADVERTISEMENT BANNER
        if (adConfig.isEnabled) {
            item {
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131722)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "ADVERTISEMENT",
                                color = Color(0xFFFFB703),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "High CPM Video Network Ads & Sponsors",
                                color = VairalTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Button(
                            onClick = { viewModel.unlockVideoAd(videoId, context) },
                            colors = ButtonDefaults.buttonColors(containerColor = VairalRed),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text("Visit", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // RELATED VIDEOS HEADER
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Up Next & Viral Related",
                    color = VairalTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // RELATED VIDEOS LIST
        items(relatedVideos, key = { it.id }) { item ->
            VideoCard(
                video = item,
                onClick = { viewModel.openVideo(item.id) },
                onShareClick = { viewModel.openShareDialog(item) },
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = tint,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
