package com.example.ui.screens.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Post
import com.example.data.model.User
import com.example.ui.theme.ElvoraBorder
import com.example.ui.theme.ElvoraCyanAccent
import com.example.ui.theme.ElvoraElectricViolet
import com.example.ui.theme.ElvoraMidnight
import com.example.ui.theme.ElvoraSilverLight
import com.example.ui.theme.ElvoraSilverMid
import com.example.ui.theme.ElvoraSilverMuted
import com.example.ui.theme.ElvoraSilverPure
import com.example.ui.theme.ElvoraSurfaceCard
import com.example.ui.theme.ElvoraSurfaceDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareBottomSheet(
    post: Post,
    friends: List<User>,
    onCopyLink: () -> Unit,
    onRepost: () -> Unit,
    onSendToFriend: (User) -> Unit,
    onAddToStory: () -> Unit,
    onExternalShare: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ElvoraMidnight,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Share Post",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElvoraSilverPure
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = ElvoraSilverLight
                    )
                }
            }

            HorizontalDivider(color = ElvoraBorder.copy(alpha = 0.5f), thickness = 0.5.dp)

            // Direct Message to Friends Row
            Text(
                text = "Send via Elvora Chat",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ElvoraSilverMid,
                modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 6.dp)
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(friends) { friend ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { onSendToFriend(friend) }
                            .padding(vertical = 4.dp)
                    ) {
                        AsyncImage(
                            model = friend.avatarUrl,
                            contentDescription = friend.name,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, ElvoraElectricViolet, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = friend.name.substringBefore(" "),
                            fontSize = 11.sp,
                            color = ElvoraSilverPure,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Send",
                            fontSize = 10.sp,
                            color = ElvoraCyanAccent,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            HorizontalDivider(
                color = ElvoraBorder.copy(alpha = 0.5f),
                thickness = 0.5.dp,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Quick Actions: Copy Link, Repost, Add to Story, External Share
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ShareActionItem(
                    icon = Icons.Default.Link,
                    label = "Copy Link",
                    onClick = onCopyLink
                )
                ShareActionItem(
                    icon = Icons.Default.Repeat,
                    label = "Repost",
                    onClick = onRepost
                )
                ShareActionItem(
                    icon = Icons.Default.Timer,
                    label = "Add to Story",
                    onClick = onAddToStory
                )
                ShareActionItem(
                    icon = Icons.Default.Share,
                    label = "Share via...",
                    onClick = onExternalShare
                )
            }
        }
    }
}

@Composable
private fun ShareActionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(ElvoraSurfaceCard)
                .border(1.dp, ElvoraBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = ElvoraSilverPure,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = ElvoraSilverLight
        )
    }
}
