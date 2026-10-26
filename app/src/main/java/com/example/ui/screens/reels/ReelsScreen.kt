package com.example.ui.screens.reels

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Post
import com.example.data.model.Reel
import com.example.data.model.User
import com.example.ui.components.HeartBurstAnimation
import com.example.ui.theme.ElvoraBorder
import com.example.ui.theme.ElvoraElectricViolet
import com.example.ui.theme.ElvoraMidnight
import com.example.ui.theme.ElvoraRoseAccent
import com.example.ui.theme.ElvoraSilverLight
import com.example.ui.theme.ElvoraSilverMid
import com.example.ui.theme.ElvoraSilverPure
import com.example.ui.theme.ElvoraSurfaceDark
import com.example.ui.theme.ElvoraVioletBright
import com.example.ui.theme.ElvoraVioletGlow

@Composable
fun ReelsScreen(
    reels: List<Reel>,
    onLikeReel: (String) -> Unit,
    onSaveReel: (String) -> Unit,
    onFollowCreator: (String) -> Unit,
    onCommentClick: (Post) -> Unit,
    onShareClick: (Post) -> Unit,
    onCreateReelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { reels.size })

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ElvoraMidnight)
            .testTag("reels_screen")
    ) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val reel = reels[page]
            ReelItemPage(
                reel = reel,
                onLike = { onLikeReel(reel.id) },
                onSave = { onSaveReel(reel.id) },
                onFollow = { onFollowCreator(reel.id) },
                onComment = {
                    // Adapt to post representation for comments sheet
                    onCommentClick(
                        Post(
                            id = reel.id,
                            author = reel.creator,
                            caption = reel.caption,
                            likesCount = reel.likesCount,
                            commentsCount = reel.commentsCount,
                            sharesCount = reel.sharesCount,
                            timeAgo = "Reel"
                        )
                    )
                },
                onShare = {
                    onShareClick(
                        Post(
                            id = reel.id,
                            author = reel.creator,
                            caption = reel.caption,
                            likesCount = reel.likesCount,
                            commentsCount = reel.commentsCount,
                            sharesCount = reel.sharesCount,
                            timeAgo = "Reel"
                        )
                    )
                }
            )
        }

        // Top Overlay Header: Reels Tab & Camera Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Reels",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ElvoraSilverPure
                )
                Text(
                    text = "Trending",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ElvoraElectricViolet
                )
                Text(
                    text = "Following",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = ElvoraSilverMid
                )
            }

            IconButton(
                onClick = onCreateReelClick,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(ElvoraMidnight.copy(alpha = 0.6f))
            ) {
                Icon(
                    imageVector = Icons.Default.Videocam,
                    contentDescription = "Create Reel",
                    tint = ElvoraSilverPure,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun ReelItemPage(
    reel: Reel,
    onLike: () -> Unit,
    onSave: () -> Unit,
    onFollow: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(true) }
    var showHeartBurst by remember { mutableStateOf(false) }

    // Audio vinyl rotation
    val infiniteTransition = rememberInfiniteTransition(label = "vinyl")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "vinyl_rotate"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { isPlaying = !isPlaying },
                    onDoubleTap = {
                        if (!reel.isLiked) onLike()
                        showHeartBurst = true
                    }
                )
            }
    ) {
        // Reel Video / Cover Visual
        if (reel.mediaRes != null) {
            Image(
                painter = painterResource(id = reel.mediaRes),
                contentDescription = "Reel video",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else if (reel.mediaUrl != null) {
            AsyncImage(
                model = reel.mediaUrl,
                contentDescription = "Reel video",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Gradient overlay for bottom metadata
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            ElvoraMidnight.copy(alpha = 0.35f),
                            Color.Transparent,
                            ElvoraMidnight.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // Paused Indicator
        if (!isPlaying) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .background(ElvoraMidnight.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Paused",
                    tint = ElvoraSilverPure,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        // Double-tap heart burst
        Box(
            modifier = Modifier.align(Alignment.Center)
        ) {
            HeartBurstAnimation(
                visible = showHeartBurst,
                onAnimationEnd = { showHeartBurst = false }
            )
        }

        // Bottom Creator Info & Caption
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.8f)
                .navigationBarsPadding()
                .padding(start = 16.dp, bottom = 68.dp)
        ) {
            // Creator info & Follow button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = reel.creator.avatarUrl,
                    contentDescription = reel.creator.name,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.dp, ElvoraElectricViolet, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = "@${reel.creator.username}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElvoraSilverPure
                )

                // Follow Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (reel.isFollowingCreator) ElvoraSurfaceDark else ElvoraElectricViolet
                        )
                        .border(
                            1.dp,
                            if (reel.isFollowingCreator) ElvoraBorder else ElvoraSilverPure.copy(alpha = 0.3f),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable(onClick = onFollow)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (reel.isFollowingCreator) "Following" else "Follow",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ElvoraSilverPure
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Caption
            Text(
                text = reel.caption,
                fontSize = 13.sp,
                color = ElvoraSilverLight,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Audio track ticker
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(ElvoraMidnight.copy(alpha = 0.5f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Music",
                    tint = ElvoraSilverPure,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = reel.audioTrack,
                    fontSize = 11.sp,
                    color = ElvoraSilverPure,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Right Action Bar: Like, Comment, Share, Save, Rotating Disc
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(end = 12.dp, bottom = 68.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Like
            ReelActionButton(
                icon = if (reel.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                label = "${reel.likesCount}",
                tint = if (reel.isLiked) ElvoraRoseAccent else ElvoraSilverPure,
                onClick = onLike
            )

            // Comment
            ReelActionButton(
                icon = Icons.Outlined.ChatBubbleOutline,
                label = "${reel.commentsCount}",
                tint = ElvoraSilverPure,
                onClick = onComment
            )

            // Share
            ReelActionButton(
                icon = Icons.Outlined.Share,
                label = "${reel.sharesCount}",
                tint = ElvoraSilverPure,
                onClick = onShare
            )

            // Save
            ReelActionButton(
                icon = if (reel.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                label = if (reel.isSaved) "Saved" else "Save",
                tint = if (reel.isSaved) ElvoraElectricViolet else ElvoraSilverPure,
                onClick = onSave
            )

            // Rotating Metallic Vinyl Disc
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .rotate(if (isPlaying) rotation else 0f)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(ElvoraSilverPure, ElvoraMidnight, ElvoraVioletGlow)
                        )
                    )
                    .border(2.dp, ElvoraSilverMid, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(ElvoraMidnight)
                )
            }
        }
    }
}

@Composable
private fun ReelActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = ElvoraSilverPure
        )
    }
}
