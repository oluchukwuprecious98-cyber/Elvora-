package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ElvoraElectricViolet
import com.example.ui.theme.ElvoraRoseAccent

@Composable
fun HeartBurstAnimation(
    visible: Boolean,
    onAnimationEnd: () -> Unit
) {
    val scale = remember { Animatable(0.4f) }

    LaunchedEffect(visible) {
        if (visible) {
            scale.snapTo(0.4f)
            scale.animateTo(
                targetValue = 1.25f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            scale.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(150)
            )
            kotlinx.coroutines.delay(400)
            onAnimationEnd()
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(150)) + scaleIn(tween(150)),
        exit = fadeOut(tween(250)) + scaleOut(tween(250))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = ElvoraRoseAccent,
                modifier = Modifier.size((90 * scale.value).dp)
            )
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = ElvoraElectricViolet.copy(alpha = 0.6f),
                modifier = Modifier.size((110 * scale.value).dp)
            )
        }
    }
}
