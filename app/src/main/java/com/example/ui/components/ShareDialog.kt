package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.VairalCard
import com.example.ui.theme.VairalRed
import com.example.ui.theme.VairalSurfaceVariant
import com.example.ui.theme.VairalTextPrimary
import com.example.ui.theme.VairalTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareDialog(
    videoTitle: String,
    shareUrl: String,
    onDismiss: () -> Unit,
    onShareTracked: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = VairalCard,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Share Video",
                    color = VairalTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = VairalTextSecondary
                    )
                }
            }

            Text(
                text = videoTitle,
                color = VairalTextSecondary,
                fontSize = 13.sp,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Quick App Share Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ShareAppItem(
                    label = "WhatsApp",
                    icon = Icons.Default.Forum,
                    bgColor = Color(0xFF25D366),
                    onClick = {
                        shareToSpecificPackage(
                            context = context,
                            pkg = "com.whatsapp",
                            text = "Check out this video on VAIRAL 6T9: $videoTitle\n$shareUrl"
                        )
                        onShareTracked()
                        onDismiss()
                    }
                )

                ShareAppItem(
                    label = "Telegram",
                    icon = Icons.Default.Send,
                    bgColor = Color(0xFF229ED9),
                    onClick = {
                        shareToSpecificPackage(
                            context = context,
                            pkg = "org.telegram.messenger",
                            text = "Check out this video on VAIRAL 6T9: $videoTitle\n$shareUrl"
                        )
                        onShareTracked()
                        onDismiss()
                    }
                )

                ShareAppItem(
                    label = "Facebook",
                    icon = Icons.Default.Public,
                    bgColor = Color(0xFF1877F2),
                    onClick = {
                        shareToSpecificPackage(
                            context = context,
                            pkg = "com.facebook.katana",
                            text = shareUrl
                        )
                        onShareTracked()
                        onDismiss()
                    }
                )

                ShareAppItem(
                    label = "More",
                    icon = Icons.Default.Share,
                    bgColor = VairalRed,
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Watch \"$videoTitle\" on VAIRAL 6T9: $shareUrl")
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Share video via")
                        context.startActivity(shareIntent)
                        onShareTracked()
                        onDismiss()
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Copy Link Box
            Card(
                colors = CardDefaults.cardColors(containerColor = VairalSurfaceVariant),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = shareUrl,
                        color = VairalTextSecondary,
                        fontSize = 13.sp,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("VAIRAL 6T9 Video URL", shareUrl)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Link copied to clipboard!", Toast.LENGTH_SHORT).show()
                            onShareTracked()
                        },
                        modifier = Modifier.testTag("copy_link_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Link",
                            tint = VairalRed
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ShareAppItem(
    label: String,
    icon: ImageVector,
    bgColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            color = VairalTextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun shareToSpecificPackage(context: Context, pkg: String, text: String) {
    try {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            setPackage(pkg)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback to standard chooser if target package isn't installed
        val fallback = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(Intent.createChooser(fallback, "Share via"))
    }
}
