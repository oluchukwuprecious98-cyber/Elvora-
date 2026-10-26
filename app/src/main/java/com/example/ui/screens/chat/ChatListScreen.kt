package com.example.ui.screens.chat

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.ChatConversation
import com.example.ui.theme.ElvoraBorder
import com.example.ui.theme.ElvoraBorderLight
import com.example.ui.theme.ElvoraElectricViolet
import com.example.ui.theme.ElvoraGreenOnline
import com.example.ui.theme.ElvoraMidnight
import com.example.ui.theme.ElvoraSilverLight
import com.example.ui.theme.ElvoraSilverMid
import com.example.ui.theme.ElvoraSilverMuted
import com.example.ui.theme.ElvoraSilverPure
import com.example.ui.theme.ElvoraSurfaceCard
import com.example.ui.theme.ElvoraSurfaceDark

@Composable
fun ChatListScreen(
    conversations: List<ChatConversation>,
    onConversationClick: (ChatConversation) -> Unit,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredConversations = remember(conversations, searchQuery) {
        if (searchQuery.isBlank()) conversations
        else conversations.filter {
            it.otherUser.name.contains(searchQuery, ignoreCase = true) ||
            it.otherUser.username.contains(searchQuery, ignoreCase = true) ||
            it.lastMessage.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ElvoraMidnight)
            .statusBarsPadding()
            .testTag("chat_list_screen")
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = ElvoraSilverPure
                    )
                }
                Text(
                    text = "Elvora Chat",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElvoraSilverPure
                )
            }

            IconButton(onClick = { /* New conversation */ }) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "New Message",
                    tint = ElvoraElectricViolet
                )
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search messages & friends...", color = ElvoraSilverMuted, fontSize = 13.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = ElvoraSilverMuted,
                    modifier = Modifier.size(20.dp)
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = ElvoraSurfaceDark,
                unfocusedContainerColor = ElvoraSurfaceDark,
                focusedBorderColor = ElvoraElectricViolet,
                unfocusedBorderColor = ElvoraBorder,
                focusedTextColor = ElvoraSilverPure,
                unfocusedTextColor = ElvoraSilverPure
            )
        )

        // Active Contacts Horizontal Carousel
        Text(
            text = "Active Now",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ElvoraSilverMid,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 6.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(conversations) { conv ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onConversationClick(conv) }
                        .width(56.dp)
                ) {
                    Box {
                        AsyncImage(
                            model = conv.otherUser.avatarUrl,
                            contentDescription = conv.otherUser.name,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, ElvoraElectricViolet, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        if (conv.isOnline) {
                            Box(
                                modifier = Modifier
                                    .size(13.dp)
                                    .clip(CircleShape)
                                    .background(ElvoraGreenOnline)
                                    .border(2.dp, ElvoraMidnight, CircleShape)
                                    .align(Alignment.BottomEnd)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = conv.otherUser.name.substringBefore(" "),
                        fontSize = 11.sp,
                        color = ElvoraSilverPure,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        HorizontalDivider(
            color = ElvoraBorder.copy(alpha = 0.5f),
            thickness = 0.5.dp,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        // Conversations List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(filteredConversations, key = { it.id }) { conv ->
                ConversationRow(
                    conversation = conv,
                    onClick = { onConversationClick(conv) }
                )
            }
        }
    }
}

@Composable
private fun ConversationRow(
    conversation: ChatConversation,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box {
                AsyncImage(
                    model = conversation.otherUser.avatarUrl,
                    contentDescription = conversation.otherUser.name,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(1.dp, ElvoraBorder, CircleShape),
                    contentScale = ContentScale.Crop
                )
                if (conversation.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(ElvoraGreenOnline)
                            .border(2.dp, ElvoraMidnight, CircleShape)
                            .align(Alignment.BottomEnd)
                    )
                }
            }

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = conversation.otherUser.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElvoraSilverPure
                    )
                    if (conversation.otherUser.isVerified) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Verified",
                            tint = ElvoraElectricViolet,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = conversation.lastMessage,
                    fontSize = 12.sp,
                    color = if (conversation.unreadCount > 0) ElvoraSilverPure else ElvoraSilverMuted,
                    fontWeight = if (conversation.unreadCount > 0) FontWeight.SemiBold else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = conversation.lastTime,
                fontSize = 11.sp,
                color = ElvoraSilverMuted
            )
            if (conversation.unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(ElvoraElectricViolet),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = conversation.unreadCount.toString(),
                        color = ElvoraSilverPure,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
