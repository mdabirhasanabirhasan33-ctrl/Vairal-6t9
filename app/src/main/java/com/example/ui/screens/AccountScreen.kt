package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.VideoCard
import com.example.ui.theme.VairalCard
import com.example.ui.theme.VairalDarkBg
import com.example.ui.theme.VairalRed
import com.example.ui.theme.VairalSurfaceVariant
import com.example.ui.theme.VairalTextPrimary
import com.example.ui.theme.VairalTextSecondary
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(
    viewModel: MainViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val watchHistory by viewModel.watchHistory.collectAsState()
    val savedVideos by viewModel.savedVideos.collectAsState()
    val allVideos by viewModel.publishedVideos.collectAsState()

    val scope = rememberCoroutineScope()
    var authTab by remember { mutableIntStateOf(0) } // 0: Login, 1: Register
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var confirmPasswordInput by remember { mutableStateOf("") }
    var displayNameInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var profileTab by remember { mutableIntStateOf(0) } // 0: Saved Videos, 1: Watch History

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(VairalDarkBg),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        if (currentUser == null) {
            // AUTHENTICATION CARDS (LOGIN / REGISTER)
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = VairalCard),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Welcome to VAIRAL 6T9",
                            color = VairalTextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Login or register to save videos and view watch history",
                            color = VairalTextSecondary,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        TabRow(
                            selectedTabIndex = authTab,
                            containerColor = VairalSurfaceVariant,
                            contentColor = Color.White,
                            indicator = { tabPositions ->
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[authTab]),
                                    color = VairalRed
                                )
                            }
                        ) {
                            Tab(
                                selected = authTab == 0,
                                onClick = { authTab = 0; errorMessage = null },
                                text = { Text("Sign In", fontWeight = FontWeight.Bold) }
                            )
                            Tab(
                                selected = authTab == 1,
                                onClick = { authTab = 1; errorMessage = null },
                                text = { Text("Register", fontWeight = FontWeight.Bold) }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (authTab == 1) {
                            OutlinedTextField(
                                value = displayNameInput,
                                onValueChange = { displayNameInput = it },
                                label = { Text("Display Name") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = VairalRed,
                                    unfocusedBorderColor = Color(0xFF2E3244),
                                    focusedTextColor = VairalTextPrimary,
                                    unfocusedTextColor = VairalTextPrimary
                                ),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        OutlinedTextField(
                            value = emailInput,
                            onValueChange = { emailInput = it },
                            label = { Text("Email Address") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VairalRed,
                                unfocusedBorderColor = Color(0xFF2E3244),
                                focusedTextColor = VairalTextPrimary,
                                unfocusedTextColor = VairalTextPrimary
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_email_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = passwordInput,
                            onValueChange = { passwordInput = it },
                            label = { Text("Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VairalRed,
                                unfocusedBorderColor = Color(0xFF2E3244),
                                focusedTextColor = VairalTextPrimary,
                                unfocusedTextColor = VairalTextPrimary
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_password_input")
                        )

                        if (authTab == 1) {
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value = confirmPasswordInput,
                                onValueChange = { confirmPasswordInput = it },
                                label = { Text("Confirm Password") },
                                visualTransformation = PasswordVisualTransformation(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = VairalRed,
                                    unfocusedBorderColor = Color(0xFF2E3244),
                                    focusedTextColor = VairalTextPrimary,
                                    unfocusedTextColor = VairalTextPrimary
                                ),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        if (errorMessage != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = errorMessage ?: "",
                                color = VairalRed,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    if (authTab == 0) {
                                        // Login
                                        val res = viewModel.authRepo.login(emailInput)
                                        res.fold(
                                            onSuccess = {
                                                errorMessage = null
                                                viewModel.showMessage("Welcome back, ${it.displayName}!")
                                            },
                                            onFailure = {
                                                errorMessage = it.message
                                            }
                                        )
                                    } else {
                                        // Register
                                        if (passwordInput != confirmPasswordInput) {
                                            errorMessage = "Passwords do not match."
                                            return@launch
                                        }
                                        val res = viewModel.authRepo.register(emailInput, displayNameInput)
                                        res.fold(
                                            onSuccess = {
                                                errorMessage = null
                                                viewModel.showMessage("Account created! Logged in.")
                                            },
                                            onFailure = {
                                                errorMessage = it.message
                                            }
                                        )
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = VairalRed),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("auth_submit_button")
                        ) {
                            Text(
                                text = if (authTab == 0) "Sign In" else "Create Account",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Demo Quick Login
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Quick Demo User",
                                color = VairalRed,
                                fontSize = 12.sp,
                                modifier = Modifier.clickable {
                                    emailInput = "creator@vairal6t9.com"
                                    passwordInput = "demo123"
                                }
                            )

                            Text(
                                text = "Admin Demo",
                                color = Color(0xFFFFB703),
                                fontSize = 12.sp,
                                modifier = Modifier.clickable {
                                    emailInput = "admin@vairal6t9.com"
                                    passwordInput = "admin123"
                                }
                            )
                        }
                    }
                }
            }
        } else {
            // USER LOGGED IN PROFILE CARD
            val user = currentUser!!
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = VairalCard),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(VairalRed),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = user.displayName.take(1).uppercase(),
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = user.displayName,
                                        color = VairalTextPrimary,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = user.email,
                                        color = VairalTextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (user.role == "ADMIN") Color(0xFF2E3244) else VairalSurfaceVariant)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = user.role,
                                    color = if (user.role == "ADMIN") Color(0xFFFFB703) else VairalRed,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.authRepo.logout() },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = VairalTextSecondary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Sign Out", fontSize = 12.sp)
                            }

                            if (user.role == "ADMIN") {
                                Button(
                                    onClick = { onNavigate(AppScreen.ADMIN_DASHBOARD) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB703)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Open Admin Panel", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // TABS: SAVED VIDEOS & WATCH HISTORY
            item {
                TabRow(
                    selectedTabIndex = profileTab,
                    containerColor = VairalCard,
                    contentColor = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[profileTab]),
                            color = VairalRed
                        )
                    }
                ) {
                    Tab(
                        selected = profileTab == 0,
                        onClick = { profileTab = 0 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Saved (${savedVideos.size})")
                            }
                        }
                    )
                    Tab(
                        selected = profileTab == 1,
                        onClick = { profileTab = 1 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("History (${watchHistory.size})")
                            }
                        }
                    )
                }
            }

            val displayedVideos = if (profileTab == 0) {
                val savedIds = savedVideos.map { it.videoId }.toSet()
                allVideos.filter { it.id in savedIds }
            } else {
                val historyIds = watchHistory.map { it.videoId }.toSet()
                allVideos.filter { it.id in historyIds }
            }

            if (displayedVideos.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (profileTab == 0) "No saved videos yet." else "No watch history recorded.",
                            color = VairalTextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                items(displayedVideos, key = { it.id }) { video ->
                    VideoCard(
                        video = video,
                        onClick = { viewModel.openVideo(video.id) },
                        onShareClick = { viewModel.openShareDialog(video) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // ADMIN PANEL PORTAL BANNER
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF141724)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { onNavigate(AppScreen.ADMIN_DASHBOARD) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                                .background(Color(0xFF262C40)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Admin",
                                tint = Color(0xFFFFB703),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "VAIRAL 6T9 ADMIN APP",
                                color = VairalTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Upload, manage videos, users & live ad settings",
                                color = VairalTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Button(
                        onClick = { onNavigate(AppScreen.ADMIN_DASHBOARD) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF262C40)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Open", color = Color(0xFFFFB703), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
