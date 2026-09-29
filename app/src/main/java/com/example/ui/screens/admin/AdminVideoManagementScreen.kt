package com.example.ui.screens.admin

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.ui.MainViewModel
import com.example.ui.theme.VairalAccentGold
import com.example.ui.theme.VairalCard
import com.example.ui.theme.VairalDarkBg
import com.example.ui.theme.VairalGreen
import com.example.ui.theme.VairalRed
import com.example.ui.theme.VairalSurfaceVariant
import com.example.ui.theme.VairalTextPrimary
import com.example.ui.theme.VairalTextSecondary

@Composable
fun AdminVideoManagementScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    BackHandler { onBack() }

    val adminVideos by viewModel.adminVideos.collectAsState()
    var showUploadDialog by remember { mutableStateOf(false) }

    // Upload form states
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Viral") }
    var videoUrl by remember { mutableStateOf("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4") }
    var thumbnailUrl by remember { mutableStateOf("https://images.unsplash.com/photo-1536240478700-b869070f9279?w=800&auto=format&fit=crop&q=80") }
    var tags by remember { mutableStateOf("viral, hot, 6t9") }
    var duration by remember { mutableStateOf("0:30") }
    var isTrending by remember { mutableStateOf(true) }
    var published by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VairalDarkBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = VairalTextPrimary)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Manage Videos (${adminVideos.size})",
                            color = VairalTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = { showUploadDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = VairalRed),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("admin_upload_video_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Upload", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (adminVideos.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No videos currently found. Click Upload to add your first video.", color = VairalTextSecondary)
                    }
                }
            } else {
                items(adminVideos, key = { it.id }) { video ->
                    AdminVideoRowItem(
                        video = video,
                        onTogglePublish = {
                            viewModel.togglePublish(video.id, video.published)
                        },
                        onDelete = {
                            viewModel.deleteVideo(video.id)
                        },
                        onCopyUrl = {
                            val url = "https://vairal6t9.web.app/video/${video.id}"
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Video URL", url))
                            Toast.makeText(context, "URL copied: $url", Toast.LENGTH_SHORT).show()
                        },
                        onPlay = {
                            viewModel.openVideo(video.id)
                        }
                    )
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { showUploadDialog = true },
            containerColor = VairalRed,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Upload Video")
        }

        // UPLOAD VIDEO MODAL DIALOG
        if (showUploadDialog) {
            AlertDialog(
                onDismissRequest = { showUploadDialog = false },
                containerColor = VairalCard,
                title = {
                    Text("Upload New Video", color = VairalTextPrimary, fontWeight = FontWeight.Bold)
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Video Title") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VairalRed,
                                focusedTextColor = VairalTextPrimary,
                                unfocusedTextColor = VairalTextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            maxLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VairalRed,
                                focusedTextColor = VairalTextPrimary,
                                unfocusedTextColor = VairalTextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = category,
                                onValueChange = { category = it },
                                label = { Text("Category") },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = VairalRed,
                                    focusedTextColor = VairalTextPrimary,
                                    unfocusedTextColor = VairalTextPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = duration,
                                onValueChange = { duration = it },
                                label = { Text("Duration") },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = VairalRed,
                                    focusedTextColor = VairalTextPrimary,
                                    unfocusedTextColor = VairalTextPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = videoUrl,
                            onValueChange = { videoUrl = it },
                            label = { Text("Video Stream/File URL (MP4, WebM)") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VairalRed,
                                focusedTextColor = VairalTextPrimary,
                                unfocusedTextColor = VairalTextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = thumbnailUrl,
                            onValueChange = { thumbnailUrl = it },
                            label = { Text("Thumbnail Image URL") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VairalRed,
                                focusedTextColor = VairalTextPrimary,
                                unfocusedTextColor = VairalTextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = tags,
                            onValueChange = { tags = it },
                            label = { Text("Tags (comma separated)") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VairalRed,
                                focusedTextColor = VairalTextPrimary,
                                unfocusedTextColor = VairalTextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Featured Trending", color = VairalTextPrimary, fontSize = 13.sp)
                            Switch(
                                checked = isTrending,
                                onCheckedChange = { isTrending = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = VairalRed)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Publish Immediately", color = VairalTextPrimary, fontSize = 13.sp)
                            Switch(
                                checked = published,
                                onCheckedChange = { published = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = VairalGreen)
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                viewModel.uploadVideo(
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
                                showUploadDialog = false
                                title = ""
                                description = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VairalRed)
                    ) {
                        Text("Confirm Upload")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showUploadDialog = false }) {
                        Text("Cancel", color = VairalTextSecondary)
                    }
                }
            )
        }
    }
}

@Composable
private fun AdminVideoRowItem(
    video: VideoEntity,
    onTogglePublish: () -> Unit,
    onDelete: () -> Unit,
    onCopyUrl: () -> Unit,
    onPlay: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = VairalCard),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 5.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (video.published) VairalGreen.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (video.published) "PUBLISHED" else "UNPUBLISHED",
                            color = if (video.published) VairalGreen else Color.Red,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "ID: ${video.id}",
                        color = VairalTextSecondary,
                        fontSize = 11.sp
                    )
                }

                Text(
                    text = video.category,
                    color = VairalAccentGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = video.title,
                color = VairalTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${video.views} views • ${video.shares} shares • ${video.duration}",
                color = VairalTextSecondary,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Admin Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPlay) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Preview Play", tint = VairalGreen)
                }

                IconButton(onClick = onCopyUrl) {
                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy Link", tint = VairalTextSecondary)
                }

                IconButton(onClick = onTogglePublish) {
                    Icon(
                        imageVector = if (video.published) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Publish",
                        tint = if (video.published) VairalGreen else Color.Gray
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}
