package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.navigation.Screen
import com.example.ui.theme.ElvoraBorder
import com.example.ui.theme.ElvoraElectricViolet
import com.example.ui.theme.ElvoraMidnight
import com.example.ui.theme.ElvoraSilverLight
import com.example.ui.theme.ElvoraSilverMuted
import com.example.ui.theme.ElvoraSilverPure
import com.example.ui.theme.ElvoraSurfaceDark

@Composable
fun ElvoraBottomNav(
    currentRoute: String?,
    userAvatarUrl: String,
    unreadChatCount: Int = 0,
    onNavigate: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ElvoraMidnight)
            .border(
                width = 1.dp,
                color = ElvoraBorder.copy(alpha = 0.5f),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(ElvoraSurfaceDark)
            .navigationBarsPadding()
            .height(58.dp)
            .testTag("elvora_bottom_nav"),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Home
            BottomNavItem(
                isSelected = currentRoute == Screen.Home.route,
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                label = "Home",
                tag = "nav_home",
                onClick = { onNavigate(Screen.Home.route) }
            )

            // 2. Reels
            BottomNavItem(
                isSelected = currentRoute == Screen.Reels.route,
                selectedIcon = Icons.Filled.PlayCircle,
                unselectedIcon = Icons.Outlined.PlayCircleOutline,
                label = "Reels",
                tag = "nav_reels",
                onClick = { onNavigate(Screen.Reels.route) }
            )

            // 3. Moments (24-Hour Stories)
            BottomNavItem(
                isSelected = currentRoute == Screen.Moments.route,
                selectedIcon = Icons.Filled.Timer,
                unselectedIcon = Icons.Outlined.Timer,
                label = "Moments",
                tag = "nav_moments",
                onClick = { onNavigate(Screen.Moments.route) }
            )

            // 4. Chat
            BottomNavItem(
                isSelected = currentRoute == Screen.Chat.route,
                selectedIcon = Icons.Filled.ChatBubble,
                unselectedIcon = Icons.Outlined.ChatBubbleOutline,
                label = "Chat",
                tag = "nav_chat",
                badgeCount = unreadChatCount,
                onClick = { onNavigate(Screen.Chat.route) }
            )

            // 5. Profile
            val isProfileSelected = currentRoute == Screen.Profile.route
            val avatarBorderColor by animateColorAsState(
                targetValue = if (isProfileSelected) ElvoraElectricViolet else Color.Transparent,
                animationSpec = tween(200),
                label = "avatar_border"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .size(52.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onNavigate(Screen.Profile.route) }
                    )
                    .testTag("nav_profile")
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, avatarBorderColor, CircleShape)
                        .padding(1.5.dp)
                ) {
                    AsyncImage(
                        model = userAvatarUrl,
                        contentDescription = "My Profile",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
                Text(
                    text = "Profile",
                    fontSize = 10.sp,
                    color = if (isProfileSelected) ElvoraSilverPure else ElvoraSilverMuted,
                    fontWeight = if (isProfileSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    label: String,
    tag: String,
    badgeCount: Int = 0,
    onClick: () -> Unit
) {
    val tintColor by animateColorAsState(
        targetValue = if (isSelected) ElvoraSilverPure else ElvoraSilverMuted,
        animationSpec = tween(200),
        label = "nav_icon_tint"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .size(52.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .testTag(tag)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Icon(
                imageVector = if (isSelected) selectedIcon else unselectedIcon,
                contentDescription = label,
                tint = tintColor,
                modifier = Modifier.size(23.dp)
            )

            if (badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .padding(top = 0.dp, end = 0.dp)
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(ElvoraElectricViolet)
                        .border(1.dp, ElvoraMidnight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (badgeCount > 9) "9+" else badgeCount.toString(),
                        color = ElvoraSilverPure,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Text(
            text = label,
            fontSize = 10.sp,
            color = if (isSelected) ElvoraSilverPure else ElvoraSilverMuted,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
