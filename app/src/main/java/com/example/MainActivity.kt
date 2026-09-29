package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.ShareDialog
import com.example.ui.components.VairalBottomBar
import com.example.ui.components.VairalTopBar
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.TrendingScreen
import com.example.ui.screens.VideoPlayerScreen
import com.example.ui.screens.admin.AdminAdSettingsScreen
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.admin.AdminUserManagementScreen
import com.example.ui.screens.admin.AdminVideoManagementScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.VairalDarkBg

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Handle Deep Link if app opened via URL
        handleIntent(intent)

        setContent {
            MyApplicationTheme {
                VairalApp(viewModel = viewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val data = intent?.data ?: return
        // Formats: https://vairal6t9.web.app/video/{id}, https://vairal6t9.com/video/{id}, or vairal6t9://video/{id}
        val path = data.path ?: ""
        val videoId = when {
            path.startsWith("/video/") -> path.substringAfter("/video/")
            data.scheme == "vairal6t9" -> data.lastPathSegment
            else -> data.getQueryParameter("v")
        }
        if (!videoId.isNullOrBlank()) {
            viewModel.openVideo(videoId)
        }
    }
}

@Composable
fun VairalApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val selectedVideoId by viewModel.selectedVideoId.collectAsState()
    val shareDialogInfo by viewModel.shareDialogInfo.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    val isUserTabScreen = currentScreen in listOf(
        AppScreen.HOME,
        AppScreen.TRENDING,
        AppScreen.SEARCH,
        AppScreen.ACCOUNT
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(VairalDarkBg),
        topBar = {
            val backAction: (() -> Unit)? = when (currentScreen) {
                AppScreen.VIDEO_PLAYER -> {
                    { viewModel.navigateTo(AppScreen.HOME) }
                }
                AppScreen.ADMIN_VIDEOS,
                AppScreen.ADMIN_USERS,
                AppScreen.ADMIN_ADS -> {
                    { viewModel.navigateTo(AppScreen.ADMIN_DASHBOARD) }
                }
                AppScreen.ADMIN_DASHBOARD -> {
                    { viewModel.navigateTo(AppScreen.HOME) }
                }
                else -> null
            }

            VairalTopBar(
                currentScreen = currentScreen,
                onNavigate = { viewModel.navigateTo(it) },
                onBack = backAction
            )
        },
        bottomBar = {
            if (isUserTabScreen) {
                VairalBottomBar(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = VairalDarkBg
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> {
                    HomeScreen(viewModel = viewModel)
                }
                AppScreen.TRENDING -> {
                    TrendingScreen(viewModel = viewModel)
                }
                AppScreen.SEARCH -> {
                    SearchScreen(viewModel = viewModel)
                }
                AppScreen.ACCOUNT -> {
                    AccountScreen(
                        viewModel = viewModel,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }
                AppScreen.VIDEO_PLAYER -> {
                    VideoPlayerScreen(
                        videoId = selectedVideoId ?: "v-6t9-01",
                        viewModel = viewModel,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }
                AppScreen.ADMIN_LOGIN,
                AppScreen.ADMIN_DASHBOARD -> {
                    AdminDashboardScreen(
                        viewModel = viewModel,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }
                AppScreen.ADMIN_VIDEOS -> {
                    AdminVideoManagementScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.navigateTo(AppScreen.ADMIN_DASHBOARD) }
                    )
                }
                AppScreen.ADMIN_USERS -> {
                    AdminUserManagementScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.navigateTo(AppScreen.ADMIN_DASHBOARD) }
                    )
                }
                AppScreen.ADMIN_ADS -> {
                    AdminAdSettingsScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.navigateTo(AppScreen.ADMIN_DASHBOARD) }
                    )
                }
            }

            // Social Sharing Dialog
            shareDialogInfo?.let { shareInfo ->
                ShareDialog(
                    videoTitle = shareInfo.videoTitle,
                    shareUrl = shareInfo.shareUrl,
                    onDismiss = { viewModel.closeShareDialog() },
                    onShareTracked = {
                        selectedVideoId?.let { id -> viewModel.onSharePerformed(id) }
                    }
                )
            }
        }
    }
}
