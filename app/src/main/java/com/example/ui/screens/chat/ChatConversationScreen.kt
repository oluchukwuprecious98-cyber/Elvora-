package com.example.ui.screens.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.ChatConversation
import com.example.data.model.ChatMessage
import com.example.ui.theme.ElvoraBorder
import com.example.ui.theme.ElvoraCyanAccent
import com.example.ui.theme.ElvoraElectricViolet
import com.example.ui.theme.ElvoraGreenOnline
import com.example.ui.theme.ElvoraMidnight
import com.example.ui.theme.ElvoraRoseAccent
import com.example.ui.theme.ElvoraSilverLight
import com.example.ui.theme.ElvoraSilverMid
import com.example.ui.theme.ElvoraSilverMuted
import com.example.ui.theme.ElvoraSilverPure
import com.example.ui.theme.ElvoraSurfaceCard
import com.example.ui.theme.ElvoraSurfaceDark
import com.example.ui.theme.ElvoraVioletBright
import com.example.ui.theme.ElvoraVioletGlow

@Composable
fun ChatConversationScreen(
    conversation: ChatConversation,
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onSendVoiceNote: (Int) -> Unit,
    onReactMessage: (String, String) -> Unit,
    onBack: () -> Unit,
    onShowSnack: (String) -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ElvoraMidnight)
            .statusBarsPadding()
            .testTag("chat_conversation_screen")
    ) {
        // Conversation Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ElvoraSurfaceDark)
                .border(1.dp, ElvoraBorder.copy(alpha = 0.5f), RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = ElvoraSilverPure
                    )
                }

                Box {
                    AsyncImage(
                        model = conversation.otherUser.avatarUrl,
                        contentDescription = conversation.otherUser.name,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(1.dp, ElvoraElectricViolet, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    if (conversation.isOnline) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(ElvoraGreenOnline)
                                .border(1.5.dp, ElvoraMidnight, CircleShape)
                                .align(Alignment.BottomEnd)
                        )
                    }
                }

                Column {
                    Text(
                        text = conversation.otherUser.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElvoraSilverPure
                    )
                    Text(
                        text = if (conversation.isOnline) "Active now" else "Offline",
                        fontSize = 11.sp,
                        color = if (conversation.isOnline) ElvoraGreenOnline else ElvoraSilverMuted
                    )
                }
            }

            // Call Actions
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onShowSnack("Calling ${conversation.otherUser.name}...") }) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Voice Call",
                        tint = ElvoraSilverLight,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = { onShowSnack("Starting video call with ${conversation.otherUser.name}...") }) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "Video Call",
                        tint = ElvoraElectricViolet,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages, key = { it.id }) { message ->
                MessageBubble(
                    message = message,
                    onReact = { emoji -> onReactMessage(message.id, emoji) }
                )
            }
        }

        // Bottom Input Bar with Voice Note & Photo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ElvoraMidnight)
                .navigationBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Photo Attachment
            IconButton(
                onClick = {
                    onShowSnack("Photo picker opened: select media to send")
                },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(ElvoraSurfaceCard)
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Attach photo",
                    tint = ElvoraSilverMid,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Input field
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(ElvoraSurfaceDark)
                    .border(1.dp, ElvoraBorder, RoundedCornerShape(24.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                BasicTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    textStyle = TextStyle(color = ElvoraSilverPure, fontSize = 13.sp),
                    cursorBrush = SolidColor(ElvoraElectricViolet),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if (inputText.isEmpty()) {
                            Text(
                                text = "Message ${conversation.otherUser.name.substringBefore(" ")}...",
                                color = ElvoraSilverMuted,
                                fontSize = 13.sp
                            )
                        }
                        innerTextField()
                    }
                )
            }

            // Voice Note or Send Button
            if (inputText.isBlank()) {
                // Voice Note Record Button
                IconButton(
                    onClick = {
                        onSendVoiceNote(12) // simulate 12s voice note
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(ElvoraSurfaceCard)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Record Voice Note",
                        tint = ElvoraElectricViolet,
                        modifier = Modifier.size(22.dp)
                    )
                }
            } else {
                // Send Message
                IconButton(
                    onClick = {
                        onSendMessage(inputText)
                        inputText = ""
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(ElvoraElectricViolet, ElvoraVioletBright))
                        )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = ElvoraSilverPure,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: ChatMessage,
    onReact: (String) -> Unit
) {
    val isMine = message.isMine
    var showReactionPicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMine) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .clickable { showReactionPicker = !showReactionPicker }
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMine) 16.dp else 4.dp,
                        bottomEnd = if (isMine) 4.dp else 16.dp
                    )
                )
                .background(
                    if (isMine) {
                        Brush.linearGradient(
                            listOf(ElvoraElectricViolet, ElvoraVioletBright)
                        )
                    } else {
                        Brush.linearGradient(
                            listOf(ElvoraSurfaceCard, ElvoraSurfaceDark)
                        )
                    }
                )
                .border(
                    1.dp,
                    if (isMine) ElvoraSilverPure.copy(alpha = 0.2f) else ElvoraBorder,
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMine) 16.dp else 4.dp,
                        bottomEnd = if (isMine) 4.dp else 16.dp
                    )
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            if (message.isVoiceNote) {
                VoiceNoteBubbleContent(
                    durationSec = message.voiceDurationSec ?: 10,
                    isMine = isMine
                )
            } else {
                Text(
                    text = message.text.orEmpty(),
                    fontSize = 13.sp,
                    color = ElvoraSilverPure,
                    lineHeight = 18.sp
                )
            }
        }

        // Reaction Badge
        if (message.reaction != null) {
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(ElvoraSurfaceDark)
                    .border(0.5.dp, ElvoraBorder, RoundedCornerShape(10.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(text = message.reaction, fontSize = 12.sp)
            }
        }

        // Reaction Picker Popup
        AnimatedVisibility(visible = showReactionPicker) {
            Row(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(ElvoraSurfaceDark)
                    .border(1.dp, ElvoraBorder, RoundedCornerShape(16.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("❤️", "🔥", "⚡", "😮", "👏").forEach { emoji ->
                    Text(
                        text = emoji,
                        fontSize = 16.sp,
                        modifier = Modifier.clickable {
                            onReact(emoji)
                            showReactionPicker = false
                        }
                    )
                }
            }
        }

        Text(
            text = message.timestamp,
            fontSize = 10.sp,
            color = ElvoraSilverMuted,
            modifier = Modifier.padding(top = 2.dp, start = 4.dp, end = 4.dp)
        )
    }
}

@Composable
private fun VoiceNoteBubbleContent(
    durationSec: Int,
    isMine: Boolean
) {
    var isPlaying by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (isMine) ElvoraMidnight.copy(alpha = 0.4f) else ElvoraElectricViolet)
                .clickable { isPlaying = !isPlaying },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = ElvoraSilverPure,
                modifier = Modifier.size(18.dp)
            )
        }

        // Waveform Visualizer simulation
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.width(110.dp)
        ) {
            val heights = listOf(6, 12, 18, 10, 22, 14, 8, 20, 16, 24, 12, 18, 8, 14, 10)
            heights.forEach { h ->
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(if (isPlaying) (h * 1.2).dp else h.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(if (isMine) ElvoraSilverPure else ElvoraElectricViolet)
                )
            }
        }

        Text(
            text = "0:${if (durationSec < 10) "0$durationSec" else "$durationSec"}",
            fontSize = 11.sp,
            color = if (isMine) ElvoraSilverLight else ElvoraSilverMuted,
            fontWeight = FontWeight.Medium
        )
    }
}
