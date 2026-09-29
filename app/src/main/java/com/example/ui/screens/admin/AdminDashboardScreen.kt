package com.example.ui.screens.admin

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.formatNumber
import com.example.ui.theme.VairalAccentCyan
import com.example.ui.theme.VairalAccentGold
import com.example.ui.theme.VairalCard
import com.example.ui.theme.VairalDarkBg
import com.example.ui.theme.VairalGreen
import com.example.ui.theme.VairalRed
import com.example.ui.theme.VairalSurfaceVariant
import com.example.ui.theme.VairalTextPrimary
import com.example.ui.theme.VairalTextSecondary

@Composable
fun AdminDashboardScreen(
    viewModel: MainViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalVideos by viewModel.totalVideos.collectAsState()
    val totalViews by viewModel.totalViews.collectAsState()
    val totalShares by viewModel.totalShares.collectAsState()
    val totalUsers by viewModel.totalUsers.collectAsState()
    val activeUsers by viewModel.activeUsers.collectAsState()
    val adConfig by viewModel.adConfig.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(VairalDarkBg),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ADMIN CONSOLE",
                        color = VairalAccentGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Platform Analytics",
                        color = VairalTextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = { onNavigate(AppScreen.HOME) },
                    colors = ButtonDefaults.buttonColors(containerColor = VairalSurfaceVariant),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Exit to App", color = VairalTextPrimary, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // METRICS GRID
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Total Videos",
                    value = totalVideos.toString(),
                    icon = Icons.Default.VideoLibrary,
                    accentColor = VairalRed,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Total Views",
                    value = formatNumber(totalViews),
                    icon = Icons.Default.Visibility,
                    accentColor = VairalAccentCyan,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Total Shares",
                    value = formatNumber(totalShares),
                    icon = Icons.Default.Share,
                    accentColor = VairalAccentGold,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Users (Active)",
                    value = "$activeUsers / $totalUsers",
                    icon = Icons.Default.Group,
                    accentColor = VairalGreen,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // AD STATUS CARD
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = VairalCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(AppScreen.ADMIN_ADS) }
                    .testTag("admin_ad_status_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (adConfig.isEnabled) VairalGreen.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Campaign,
                                contentDescription = "Ads",
                                tint = if (adConfig.isEnabled) VairalGreen else Color.Red,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Advertisement Network",
                                color = VairalTextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (adConfig.isEnabled) "ACTIVE • Gating Enabled" else "DISABLED",
                                color = if (adConfig.isEnabled) VairalGreen else Color.Red,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Button(
                        onClick = { onNavigate(AppScreen.ADMIN_ADS) },
                        colors = ButtonDefaults.buttonColors(containerColor = VairalSurfaceVariant),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Configure", color = VairalTextPrimary, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // QUICK MANAGEMENT SECTIONS
        item {
            Text(
                text = "Admin Controls",
                color = VairalTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            AdminActionRow(
                title = "Video Management",
                subtitle = "Upload new MP4s, edit metadata, publish or delete videos",
                icon = Icons.Default.Upload,
                badgeColor = VairalRed,
                onClick = { onNavigate(AppScreen.ADMIN_VIDEOS) },
                testTag = "admin_btn_manage_videos"
            )

            Spacer(modifier = Modifier.height(10.dp))

            AdminActionRow(
                title = "User Management",
                subtitle = "View registered accounts, toggle status, and revoke access",
                icon = Icons.Default.Group,
                badgeColor = VairalAccentCyan,
                onClick = { onNavigate(AppScreen.ADMIN_USERS) },
                testTag = "admin_btn_manage_users"
            )

            Spacer(modifier = Modifier.height(10.dp))

            AdminActionRow(
                title = "Advertisement Settings",
                subtitle = "Change target Ad URL, AdMob IDs, and gating frequency",
                icon = Icons.Default.Campaign,
                badgeColor = VairalAccentGold,
                onClick = { onNavigate(AppScreen.ADMIN_ADS) },
                testTag = "admin_btn_manage_ads"
            )
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = VairalCard),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = VairalTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                color = VairalTextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
private fun AdminActionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = VairalCard),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(badgeColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = badgeColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        color = VairalTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        color = VairalTextSecondary,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
            }

            Text(">", color = VairalTextSecondary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
