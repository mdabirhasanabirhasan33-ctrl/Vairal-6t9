package com.example.ui.screens.admin

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.theme.VairalAccentCyan
import com.example.ui.theme.VairalAccentGold
import com.example.ui.theme.VairalCard
import com.example.ui.theme.VairalDarkBg
import com.example.ui.theme.VairalGreen
import com.example.ui.theme.VairalRed
import com.example.ui.theme.VairalSurfaceVariant
import com.example.ui.theme.VairalTextPrimary
import com.example.ui.theme.VairalTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAdSettingsScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    BackHandler { onBack() }

    val adConfig by viewModel.adConfig.collectAsState()

    var adUrl by remember(adConfig) { mutableStateOf(adConfig.adUrl) }
    var isEnabled by remember(adConfig) { mutableStateOf(adConfig.isEnabled) }
    var interstitialEnabled by remember(adConfig) { mutableStateOf(adConfig.interstitialEnabled) }
    var placement by remember(adConfig) { mutableStateOf(adConfig.placement) }

    var admobAppId by remember(adConfig) { mutableStateOf(adConfig.admobAppId) }
    var admobBannerId by remember(adConfig) { mutableStateOf(adConfig.admobBannerId) }
    var admobInterstitialId by remember(adConfig) { mutableStateOf(adConfig.admobInterstitialId) }
    var admobRewardedId by remember(adConfig) { mutableStateOf(adConfig.admobRewardedId) }

    var placementDropdownExpanded by remember { mutableStateOf(false) }
    val placementOptions = listOf(
        "CLICK_TO_WATCH_GATE" to "Click-To-Watch Gateway (Pre-playback)",
        "BANNER_BOTTOM" to "Bottom Inline Banner",
        "INTERSTITIAL_PRE_ROLL" to "Standard Interstitial"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(VairalDarkBg),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = VairalTextPrimary)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "Advertisement Settings",
                        color = VairalTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Dynamic ad network & AdMob monetization routing",
                        color = VairalTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // NETWORK ADVERTISEMENT CONFIGURATION
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = VairalCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Campaign, contentDescription = null, tint = VairalRed)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "High-CPM Redirect Network",
                                color = VairalTextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Switch(
                            checked = isEnabled,
                            onCheckedChange = { isEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = VairalGreen)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Target Monetization / Redirect URL",
                        color = VairalTextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(
                        value = adUrl,
                        onValueChange = { adUrl = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VairalRed,
                            unfocusedBorderColor = Color(0xFF2E3244),
                            focusedTextColor = VairalTextPrimary,
                            unfocusedTextColor = VairalTextPrimary
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_ad_url_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = {
                                try {
                                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(adUrl)).apply {
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    }
                                    context.startActivity(browserIntent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Invalid URL: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = VairalAccentCyan),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Test Ad Destination", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Gating Flow Options
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Require 'Click To Watch' Gate", color = VairalTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text("Displays compliant unlock button before playback starts", color = VairalTextSecondary, fontSize = 11.sp)
                        }
                        Switch(
                            checked = interstitialEnabled,
                            onCheckedChange = { interstitialEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = VairalRed)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Placement Dropdown
                    Text("Ad Placement Mode", color = VairalTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))

                    ExposedDropdownMenuBox(
                        expanded = placementDropdownExpanded,
                        onExpandedChange = { placementDropdownExpanded = !placementDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = placementOptions.find { it.first == placement }?.second ?: placement,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = placementDropdownExpanded) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VairalRed,
                                unfocusedBorderColor = Color(0xFF2E3244),
                                focusedTextColor = VairalTextPrimary,
                                unfocusedTextColor = VairalTextPrimary
                            ),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = placementDropdownExpanded,
                            onDismissRequest = { placementDropdownExpanded = false }
                        ) {
                            placementOptions.forEach { opt ->
                                DropdownMenuItem(
                                    text = { Text(opt.second) },
                                    onClick = {
                                        placement = opt.first
                                        placementDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // GOOGLE ADMOB ARCHITECTURE PLACEHOLDERS
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = VairalCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = VairalAccentGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Google AdMob Configuration",
                            color = VairalTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Production placeholders ready for your official AdMob account IDs without re-compiling.",
                        color = VairalTextSecondary,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AdMobField(
                        label = "ADMOB_APP_ID",
                        value = admobAppId,
                        onValueChange = { admobAppId = it }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AdMobField(
                        label = "ADMOB_BANNER_ID",
                        value = admobBannerId,
                        onValueChange = { admobBannerId = it }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AdMobField(
                        label = "ADMOB_INTERSTITIAL_ID",
                        value = admobInterstitialId,
                        onValueChange = { admobInterstitialId = it }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AdMobField(
                        label = "ADMOB_REWARDED_ID",
                        value = admobRewardedId,
                        onValueChange = { admobRewardedId = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // SAVE BUTTON
        item {
            Button(
                onClick = {
                    viewModel.saveAdSettings(
                        adUrl = adUrl,
                        isEnabled = isEnabled,
                        interstitialEnabled = interstitialEnabled,
                        placement = placement,
                        admobAppId = admobAppId,
                        admobBannerId = admobBannerId,
                        admobInterstitialId = admobInterstitialId,
                        admobRewardedId = admobRewardedId
                    )
                    Toast.makeText(context, "Settings saved successfully!", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = VairalRed),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("admin_save_ad_settings_btn")
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Advertisement Settings", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AdMobField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(text = label, color = VairalTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(2.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VairalAccentGold,
                unfocusedBorderColor = Color(0xFF2E3244),
                focusedTextColor = VairalTextPrimary,
                unfocusedTextColor = VairalTextPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
