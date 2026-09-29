package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.theme.VairalDarkBg
import com.example.ui.theme.VairalRed
import com.example.ui.theme.VairalRedHover
import com.example.ui.theme.VairalTextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VairalTopBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    onBack: (() -> Unit)? = null
) {
    val isAdminSection = currentScreen in listOf(
        AppScreen.ADMIN_LOGIN,
        AppScreen.ADMIN_DASHBOARD,
        AppScreen.ADMIN_VIDEOS,
        AppScreen.ADMIN_USERS,
        AppScreen.ADMIN_ADS
    )

    Surface(
        color = VairalDarkBg,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onBack != null) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("topbar_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = VairalTextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }

                // Brand Logo
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        if (isAdminSection) {
                            onNavigate(AppScreen.ADMIN_DASHBOARD)
                        } else {
                            onNavigate(AppScreen.HOME)
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(VairalRed, VairalRedHover)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "VAIRAL 6T9 Logo",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "VAIRAL",
                        color = VairalTextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(VairalRed)
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "6T9",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (isAdminSection) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF2E3244))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "ADMIN",
                                color = Color(0xFFFFB703),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Actions
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!isAdminSection && currentScreen != AppScreen.SEARCH) {
                    IconButton(
                        onClick = { onNavigate(AppScreen.SEARCH) },
                        modifier = Modifier.testTag("topbar_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = VairalTextPrimary
                        )
                    }
                }

                // Switcher between User Mode and Admin Panel
                IconButton(
                    onClick = {
                        if (isAdminSection) {
                            onNavigate(AppScreen.HOME)
                        } else {
                            onNavigate(AppScreen.ADMIN_DASHBOARD)
                        }
                    },
                    modifier = Modifier.testTag("admin_switcher_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = if (isAdminSection) "Exit Admin" else "Admin Panel",
                        tint = if (isAdminSection) Color(0xFFFFB703) else VairalTextPrimary
                    )
                }
            }
        }
    }
}
