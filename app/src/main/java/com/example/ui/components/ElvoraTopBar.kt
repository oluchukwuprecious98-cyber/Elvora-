package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ElvoraBorder
import com.example.ui.theme.ElvoraElectricViolet
import com.example.ui.theme.ElvoraMidnight
import com.example.ui.theme.ElvoraRoseAccent
import com.example.ui.theme.ElvoraSilverLight
import com.example.ui.theme.ElvoraSilverPure
import com.example.ui.theme.ElvoraVioletBright
import com.example.ui.theme.ElvoraVioletGlow

@Composable
fun ElvoraTopBar(
    hasUnreadNotifications: Boolean = false,
    onCreateClick: () -> Unit = {},
    onDiscoverClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {
    val gradientTextBrush = Brush.horizontalGradient(
        colors = listOf(
            ElvoraSilverPure,
            ElvoraVioletGlow,
            ElvoraElectricViolet
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ElvoraMidnight)
            .statusBarsPadding()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo & Monogram
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Futuristic 'E' Monogram
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(ElvoraElectricViolet, ElvoraVioletBright)
                            )
                        )
                        .border(1.dp, ElvoraSilverLight.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "E",
                        color = ElvoraSilverPure,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                }

                Text(
                    text = "Elvora",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 0.5.sp,
                    style = androidx.compose.ui.text.TextStyle(
                        brush = gradientTextBrush
                    )
                )
            }

            // Top action buttons: Create, Discover, Notifications
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Create Button
                IconButton(
                    onClick = onCreateClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create",
                        tint = ElvoraSilverLight,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Discover / Explore Button
                IconButton(
                    onClick = onDiscoverClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Explore,
                        contentDescription = "Discover",
                        tint = ElvoraSilverLight,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Notifications icon
                Box(contentAlignment = Alignment.TopEnd) {
                    IconButton(
                        onClick = onNotificationsClick,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.NotificationsNone,
                            contentDescription = "Notifications",
                            tint = ElvoraSilverLight,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    if (hasUnreadNotifications) {
                        Box(
                            modifier = Modifier
                                .padding(top = 8.dp, end = 8.dp)
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(ElvoraRoseAccent)
                        )
                    }
                }
            }
        }
    }
}
