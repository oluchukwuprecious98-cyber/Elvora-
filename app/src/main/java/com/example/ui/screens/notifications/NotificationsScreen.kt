package com.example.ui.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.NotificationItem
import com.example.data.model.NotificationType
import com.example.ui.theme.ElvoraBorder
import com.example.ui.theme.ElvoraCyanAccent
import com.example.ui.theme.ElvoraElectricViolet
import com.example.ui.theme.ElvoraMidnight
import com.example.ui.theme.ElvoraRoseAccent
import com.example.ui.theme.ElvoraSilverLight
import com.example.ui.theme.ElvoraSilverMuted
import com.example.ui.theme.ElvoraSilverPure
import com.example.ui.theme.ElvoraSurfaceCard
import com.example.ui.theme.ElvoraSurfaceDark

@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem>,
    onToggleFollowBack: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Likes ❤️", "Comments 💬", "Follows 👤")

    val filteredNotifications = remember(notifications, selectedFilter) {
        when (selectedFilter) {
            "Likes ❤️" -> notifications.filter { it.type == NotificationType.LIKE }
            "Comments 💬" -> notifications.filter { it.type == NotificationType.COMMENT }
            "Follows 👤" -> notifications.filter { it.type == NotificationType.FOLLOW }
            else -> notifications
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ElvoraMidnight)
            .statusBarsPadding()
            .testTag("notifications_screen")
    ) {
        Text(
            text = "Activity",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = ElvoraSilverPure,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Filter chips
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { filter ->
                val isSelected = filter == selectedFilter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSelected) ElvoraElectricViolet else ElvoraSurfaceCard
                        )
                        .border(
                            1.dp,
                            if (isSelected) ElvoraSilverPure.copy(alpha = 0.3f) else ElvoraBorder,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { selectedFilter = filter }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = filter,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) ElvoraSilverPure else ElvoraSilverMuted
                    )
                }
            }
        }

        HorizontalDivider(
            color = ElvoraBorder.copy(alpha = 0.5f),
            thickness = 0.5.dp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Notifications List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 72.dp)
        ) {
            items(filteredNotifications, key = { it.id }) { notif ->
                NotificationRow(
                    notification = notif,
                    onFollowBack = { onToggleFollowBack(notif.id) }
                )
            }
        }
    }
}

@Composable
private fun NotificationRow(
    notification: NotificationItem,
    onFollowBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            // Avatar with Type badge
            Box(contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    model = notification.user.avatarUrl,
                    contentDescription = notification.user.name,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .border(1.dp, ElvoraBorder, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(
                            when (notification.type) {
                                NotificationType.LIKE -> ElvoraRoseAccent
                                NotificationType.COMMENT -> ElvoraElectricViolet
                                NotificationType.FOLLOW -> ElvoraCyanAccent
                                NotificationType.MENTION, NotificationType.REPOST -> ElvoraElectricViolet
                            }
                        )
                        .border(1.dp, ElvoraMidnight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (notification.type) {
                            NotificationType.LIKE -> Icons.Default.Favorite
                            NotificationType.COMMENT -> Icons.Default.ChatBubble
                            NotificationType.FOLLOW -> Icons.Default.PersonAdd
                            NotificationType.MENTION, NotificationType.REPOST -> Icons.Default.Repeat
                        },
                        contentDescription = null,
                        tint = ElvoraSilverPure,
                        modifier = Modifier.size(9.dp)
                    )
                }
            }

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = notification.user.username,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElvoraSilverPure
                    )
                    Text(
                        text = notification.timeAgo,
                        fontSize = 11.sp,
                        color = ElvoraSilverMuted
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = notification.content,
                    fontSize = 12.sp,
                    color = ElvoraSilverLight,
                    lineHeight = 16.sp
                )
            }
        }

        // Action: Follow back button for FOLLOW notifications
        if (notification.type == NotificationType.FOLLOW) {
            Button(
                onClick = onFollowBack,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (notification.isFollowingBack) ElvoraSurfaceCard else ElvoraElectricViolet,
                    contentColor = ElvoraSilverPure
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (notification.isFollowingBack) "Following" else "Follow Back",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
