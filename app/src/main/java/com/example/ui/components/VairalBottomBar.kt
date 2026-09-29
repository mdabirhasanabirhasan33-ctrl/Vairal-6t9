package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.theme.VairalDarkBg
import com.example.ui.theme.VairalRed
import com.example.ui.theme.VairalSurfaceVariant
import com.example.ui.theme.VairalTextPrimary
import com.example.ui.theme.VairalTextSecondary

@Composable
fun VairalBottomBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = VairalDarkBg,
        tonalElevation = androidx.compose.ui.unit.Dp(0f),
        modifier = modifier.navigationBarsPadding()
    ) {
        NavigationBarItem(
            selected = currentScreen == AppScreen.HOME,
            onClick = { onNavigate(AppScreen.HOME) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == AppScreen.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = VairalTextPrimary,
                indicatorColor = VairalRed,
                unselectedIconColor = VairalTextSecondary,
                unselectedTextColor = VairalTextSecondary
            ),
            modifier = Modifier.testTag("nav_item_home")
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.TRENDING,
            onClick = { onNavigate(AppScreen.TRENDING) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == AppScreen.TRENDING) Icons.Filled.LocalFireDepartment else Icons.Outlined.LocalFireDepartment,
                    contentDescription = "Trending"
                )
            },
            label = { Text("Trending", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = VairalTextPrimary,
                indicatorColor = VairalRed,
                unselectedIconColor = VairalTextSecondary,
                unselectedTextColor = VairalTextSecondary
            ),
            modifier = Modifier.testTag("nav_item_trending")
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.SEARCH,
            onClick = { onNavigate(AppScreen.SEARCH) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == AppScreen.SEARCH) Icons.Filled.Search else Icons.Outlined.Search,
                    contentDescription = "Search"
                )
            },
            label = { Text("Search", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = VairalTextPrimary,
                indicatorColor = VairalRed,
                unselectedIconColor = VairalTextSecondary,
                unselectedTextColor = VairalTextSecondary
            ),
            modifier = Modifier.testTag("nav_item_search")
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.ACCOUNT,
            onClick = { onNavigate(AppScreen.ACCOUNT) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == AppScreen.ACCOUNT) Icons.Filled.AccountCircle else Icons.Outlined.AccountCircle,
                    contentDescription = "Account"
                )
            },
            label = { Text("Account", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = VairalTextPrimary,
                indicatorColor = VairalRed,
                unselectedIconColor = VairalTextSecondary,
                unselectedTextColor = VairalTextSecondary
            ),
            modifier = Modifier.testTag("nav_item_account")
        )
    }
}
