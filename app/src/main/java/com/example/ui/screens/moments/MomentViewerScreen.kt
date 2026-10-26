package com.example.ui.screens.moments

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Moment
import com.example.ui.theme.ElvoraBorder
import com.example.ui.theme.ElvoraElectricViolet
import com.example.ui.theme.ElvoraMidnight
import com.example.ui.theme.ElvoraRoseAccent
import com.example.ui.theme.ElvoraSilverLight
import com.example.ui.theme.ElvoraSilverMid
import com.example.ui.theme.ElvoraSilverMuted
import com.example.ui.theme.ElvoraSilverPure
import com.example.ui.theme.ElvoraSurfaceDark

@Composable
fun MomentViewerScreen(
    moment: Moment,
    onClose: () -> Unit,
    onReplySent: (String) -> Unit,
    onReaction: (String) -> Unit
) {
    var replyText by remember { mutableStateOf("") }
    val progress = remember { Animatable(0f) }
    var isPaused by remember { mutableStateOf(false) }

    LaunchedEffect(moment.id, isPaused) {
        if (!isPaused) {
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = ((1f - progress.value) * 6000).toInt(),
                    easing = LinearEasing
                )
            )
            onClose()
        }
    }

    val gradientBrush = remember(moment.bgGradient) {
        Brush.verticalGradient(
            colors = moment.bgGradient.map { Color(it) }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ElvoraMidnight)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPaused = true
                        tryAwaitRelease()
                        isPaused = false
                    }
                )
            }
            .testTag("moment_viewer_screen")
    ) {
        // Background Media or Gradient
        if (moment.mediaRes != null) {
            Image(
                painter = painterResource(id = moment.mediaRes),
                contentDescription = "Moment image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Subtle dark overlay for text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                ElvoraMidnight.copy(alpha = 0.6f),
                                Color.Transparent,
                                Color.Transparent,
                                ElvoraMidnight.copy(alpha = 0.85f)
                            )
                        )
                    )
            )
        } else if (moment.mediaUrl != null) {
            AsyncImage(
                model = moment.mediaUrl,
                contentDescription = "Moment image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            // Text-only moment on vibrant gradient canvas
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradientBrush),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = moment.textCaption,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElvoraSilverPure,
                    modifier = Modifier.padding(32.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 36.sp
                )
            }
        }

        // Top Navigation & Countdown Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // Animated Progress Bar (24h story timer)
            LinearProgressIndicator(
                progress = { progress.value },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = ElvoraElectricViolet,
                trackColor = ElvoraSilverLight.copy(alpha = 0.25f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Author Info & Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AsyncImage(
                        model = moment.author.avatarUrl,
                        contentDescription = moment.author.name,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .border(1.dp, ElvoraSilverPure, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Column {
                        Text(
                            text = moment.author.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElvoraSilverPure
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "24h expiration timer",
                                tint = ElvoraElectricViolet,
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = "${moment.postedTimeAgo} • Disappears in ${moment.hoursRemaining}h",
                                fontSize = 11.sp,
                                color = ElvoraSilverMid
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Moment",
                        tint = ElvoraSilverPure
                    )
                }
            }
        }

        // Bottom Controls: Text Caption + Emoji Reactions + Reply Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            // Moment Caption (if image present)
            if (moment.mediaRes != null || moment.mediaUrl != null) {
                if (moment.textCaption.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(ElvoraMidnight.copy(alpha = 0.7f))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = moment.textCaption,
                            fontSize = 14.sp,
                            color = ElvoraSilverPure,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Quick Emoji Reaction Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                listOf("🔥", "❤️", "⚡", "😮", "👏", "💜").forEach { emoji ->
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(ElvoraMidnight.copy(alpha = 0.5f))
                            .border(1.dp, ElvoraBorder, CircleShape)
                            .clickable { onReaction(emoji) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = emoji, fontSize = 18.sp)
                    }
                }
            }

            // Reply input bar to send message directly to creator's chat
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(ElvoraSurfaceDark.copy(alpha = 0.9f))
                    .border(1.dp, ElvoraBorder, RoundedCornerShape(24.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = replyText,
                    onValueChange = { replyText = it },
                    textStyle = TextStyle(color = ElvoraSilverPure, fontSize = 13.sp),
                    cursorBrush = SolidColor(ElvoraElectricViolet),
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField ->
                        if (replyText.isEmpty()) {
                            Text(
                                text = "Send reply to ${moment.author.name}...",
                                color = ElvoraSilverMuted,
                                fontSize = 13.sp
                            )
                        }
                        innerTextField()
                    }
                )

                IconButton(
                    onClick = {
                        if (replyText.isNotBlank()) {
                            onReplySent(replyText)
                            replyText = ""
                        }
                    },
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send Reply",
                        tint = if (replyText.isNotBlank()) ElvoraElectricViolet else ElvoraSilverMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
