package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ui.theme.ElvoraBorder
import com.example.ui.theme.ElvoraElectricViolet
import com.example.ui.theme.ElvoraMidnight
import com.example.ui.theme.ElvoraSilverLight
import com.example.ui.theme.ElvoraSilverPure
import com.example.ui.theme.ElvoraVioletGlow

@Composable
fun StoryRingAvatar(
    avatarUrl: String,
    hasStory: Boolean = false,
    isViewed: Boolean = false,
    isCurrentUser: Boolean = false,
    size: Dp = 64.dp,
    onClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "story_ring")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val gradientBrush = when {
        hasStory && !isViewed -> Brush.sweepGradient(
            colors = listOf(
                ElvoraElectricViolet,
                ElvoraSilverPure,
                ElvoraVioletGlow,
                ElvoraElectricViolet
            )
        )
        hasStory && isViewed -> Brush.sweepGradient(
            colors = listOf(
                ElvoraBorder,
                ElvoraSilverLight.copy(alpha = 0.5f),
                ElvoraBorder
            )
        )
        else -> null
    }

    Box(
        modifier = Modifier
            .size(size)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (gradientBrush != null) {
            Box(
                modifier = Modifier
                    .size(size)
                    .rotate(if (!isViewed) rotation else 0f)
                    .clip(CircleShape)
                    .background(gradientBrush)
            )
        }

        // Inner midnight ring spacer
        Box(
            modifier = Modifier
                .size(size - if (hasStory) 6.dp else 0.dp)
                .clip(CircleShape)
                .background(ElvoraMidnight)
                .padding(if (hasStory) 2.dp else 0.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(size - if (hasStory) 10.dp else 0.dp)
                    .clip(CircleShape)
                    .background(ElvoraBorder),
                contentScale = ContentScale.Crop
            )
        }

        // Plus icon badge for Current User if no story yet
        if (isCurrentUser) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(ElvoraElectricViolet)
                    .border(2.dp, ElvoraMidnight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Moment",
                    tint = ElvoraSilverPure,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
