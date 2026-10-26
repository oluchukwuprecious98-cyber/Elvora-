package com.example.ui.screens.feed

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Moment
import com.example.data.model.Post
import com.example.data.model.User
import com.example.ui.components.HeartBurstAnimation
import com.example.ui.components.StoryRingAvatar
import com.example.ui.theme.ElvoraBorder
import com.example.ui.theme.ElvoraBorderLight
import com.example.ui.theme.ElvoraCyanAccent
import com.example.ui.theme.ElvoraElectricViolet
import com.example.ui.theme.ElvoraMidnight
import com.example.ui.theme.ElvoraRoseAccent
import com.example.ui.theme.ElvoraSilverLight
import com.example.ui.theme.ElvoraSilverMid
import com.example.ui.theme.ElvoraSilverMuted
import com.example.ui.theme.ElvoraSilverPure
import com.example.ui.theme.ElvoraSurfaceCard
import com.example.ui.theme.ElvoraSurfaceDark
import com.example.ui.theme.ElvoraVioletGlow
import com.example.ui.viewmodel.FeedTab

@Composable
fun HomeScreen(
    currentUser: User,
    moments: List<Moment>,
    posts: List<Post>,
    currentTab: FeedTab,
    onTabSelected: (FeedTab) -> Unit,
    onMomentClick: (Moment) -> Unit,
    onAddMomentClick: () -> Unit,
    onLikePost: (String) -> Unit,
    onCommentClick: (Post) -> Unit,
    onShareClick: (Post) -> Unit,
    onSaveClick: (String) -> Unit,
    onRepostClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ElvoraMidnight)
            .testTag("home_feed_list")
    ) {
        // Feed Filter Tabs: For You, Following, Mutuals
        item {
            FeedTabsBar(
                selectedTab = currentTab,
                onTabSelected = onTabSelected
            )
        }

        // Moments (Stories) Carousel
        item {
            MomentsCarousel(
                currentUser = currentUser,
                moments = moments,
                onMomentClick = onMomentClick,
                onAddMomentClick = onAddMomentClick
            )
            HorizontalDivider(
                color = ElvoraBorder.copy(alpha = 0.5f),
                thickness = 0.5.dp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        // Feed Posts
        if (posts.isEmpty()) {
            item {
                EmptyFeedPlaceholder(tab = currentTab)
            }
        } else {
            items(
                items = posts,
                key = { it.id }
            ) { post ->
                PostItem(
                    post = post,
                    onLike = { onLikePost(post.id) },
                    onComment = { onCommentClick(post) },
                    onShare = { onShareClick(post) },
                    onSave = { onSaveClick(post.id) },
                    onRepost = { onRepostClick(post.id) }
                )
                HorizontalDivider(
                    color = ElvoraBorder.copy(alpha = 0.4f),
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }

        // Bottom spacer for navigation bar
        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
private fun FeedTabsBar(
    selectedTab: FeedTab,
    onTabSelected: (FeedTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FeedTabChip(
            label = "For You",
            isSelected = selectedTab == FeedTab.FOR_YOU,
            onClick = { onTabSelected(FeedTab.FOR_YOU) }
        )
        FeedTabChip(
            label = "Following",
            isSelected = selectedTab == FeedTab.FOLLOWING,
            onClick = { onTabSelected(FeedTab.FOLLOWING) }
        )
        FeedTabChip(
            label = "Mutuals ⚡",
            isSelected = selectedTab == FeedTab.MUTUALS,
            onClick = { onTabSelected(FeedTab.MUTUALS) }
        )
    }
}

@Composable
private fun FeedTabChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgBrush = if (isSelected) {
        Brush.horizontalGradient(listOf(ElvoraElectricViolet, ElvoraVioletGlow))
    } else {
        Brush.horizontalGradient(listOf(ElvoraSurfaceCard, ElvoraSurfaceCard))
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgBrush)
            .border(
                1.dp,
                if (isSelected) ElvoraSilverPure.copy(alpha = 0.3f) else ElvoraBorder,
                RoundedCornerShape(20.dp)
            )
            .clickable(
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) ElvoraSilverPure else ElvoraSilverMuted
        )
    }
}

@Composable
private fun MomentsCarousel(
    currentUser: User,
    moments: List<Moment>,
    onMomentClick: (Moment) -> Unit,
    onAddMomentClick: () -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        contentPadding = PaddingValues(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // "Your Moment" Slot
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(68.dp)
            ) {
                StoryRingAvatar(
                    avatarUrl = currentUser.avatarUrl,
                    hasStory = false,
                    isCurrentUser = true,
                    size = 62.dp,
                    onClick = onAddMomentClick
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Your Story",
                    fontSize = 11.sp,
                    color = ElvoraSilverMid,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Friends' Moments
        items(
            items = moments,
            key = { it.id }
        ) { moment ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(68.dp)
            ) {
                StoryRingAvatar(
                    avatarUrl = moment.author.avatarUrl,
                    hasStory = true,
                    isViewed = moment.isViewed,
                    isCurrentUser = false,
                    size = 62.dp,
                    onClick = { onMomentClick(moment) }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = moment.author.name.substringBefore(" "),
                    fontSize = 11.sp,
                    color = if (moment.isViewed) ElvoraSilverMuted else ElvoraSilverPure,
                    fontWeight = if (moment.isViewed) FontWeight.Normal else FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PostItem(
    post: Post,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit,
    onSave: () -> Unit,
    onRepost: () -> Unit
) {
    var showHeartBurst by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("post_card_${post.id}")
    ) {
        // Repost Banner if active
        if (post.isReposted) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Repeat,
                    contentDescription = "Reposted",
                    tint = ElvoraCyanAccent,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "You reposted this to your followers",
                    fontSize = 11.sp,
                    color = ElvoraCyanAccent,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Author Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AsyncImage(
                    model = post.author.avatarUrl,
                    contentDescription = post.author.name,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .border(1.dp, ElvoraBorder, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = post.author.username,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElvoraSilverPure
                        )
                        if (post.author.isVerified) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Verified",
                                tint = ElvoraElectricViolet,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    if (post.location != null) {
                        Text(
                            text = post.location,
                            fontSize = 11.sp,
                            color = ElvoraSilverMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = post.timeAgo,
                    fontSize = 11.sp,
                    color = ElvoraSilverMuted
                )
                IconButton(onClick = onShare) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = "Post options",
                        tint = ElvoraSilverMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Post Media with Double-Tap to Like
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(ElvoraSurfaceDark)
                .combinedClickable(
                    onClick = {},
                    onDoubleClick = {
                        if (!post.isLiked) {
                            onLike()
                        }
                        showHeartBurst = true
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (post.mediaRes != null) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = post.mediaRes),
                    contentDescription = "Post media",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.05f),
                    contentScale = ContentScale.Crop
                )
            } else if (post.mediaUrl != null) {
                AsyncImage(
                    model = post.mediaUrl,
                    contentDescription = "Post media",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.05f),
                    contentScale = ContentScale.Crop
                )
            }

            // Burst Animation on Double Tap
            HeartBurstAnimation(
                visible = showHeartBurst,
                onAnimationEnd = { showHeartBurst = false }
            )
        }

        // Action Bar: Like, Comment, Share, Repost, Save
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Heart / Like
                IconButton(
                    onClick = onLike,
                    modifier = Modifier.testTag("like_button_${post.id}")
                ) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (post.isLiked) ElvoraRoseAccent else ElvoraSilverLight,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Comment
                IconButton(
                    onClick = onComment,
                    modifier = Modifier.testTag("comment_button_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comment",
                        tint = ElvoraSilverLight,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Share / Link
                IconButton(
                    onClick = onShare,
                    modifier = Modifier.testTag("share_button_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Send,
                        contentDescription = "Share",
                        tint = ElvoraSilverLight,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Repost
                IconButton(onClick = onRepost) {
                    Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = "Repost",
                        tint = if (post.isReposted) ElvoraCyanAccent else ElvoraSilverLight,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Save / Bookmark
            IconButton(
                onClick = onSave,
                modifier = Modifier.testTag("save_button_${post.id}")
            ) {
                Icon(
                    imageVector = if (post.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Save",
                    tint = if (post.isSaved) ElvoraElectricViolet else ElvoraSilverLight,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Likes & Engagement Count
        Text(
            text = "${post.likesCount} likes • ${post.sharesCount} shares",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ElvoraSilverPure,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
        )

        // Caption
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 2.dp)
        ) {
            Text(
                text = "${post.author.username} ${post.caption}",
                fontSize = 13.sp,
                color = ElvoraSilverLight,
                lineHeight = 18.sp
            )
        }

        // Hashtags
        if (post.hashtags.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                post.hashtags.take(4).forEach { tag ->
                    Text(
                        text = "#$tag",
                        fontSize = 12.sp,
                        color = ElvoraVioletGlow,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Comments Count trigger
        if (post.commentsCount > 0) {
            Text(
                text = "View all ${post.commentsCount} comments",
                fontSize = 12.sp,
                color = ElvoraSilverMuted,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable(onClick = onComment)
            )
        }

        // Audio Track Bar
        if (post.audioTrack != null) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Audio track",
                    tint = ElvoraSilverMuted,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = post.audioTrack,
                    fontSize = 11.sp,
                    color = ElvoraSilverMuted
                )
            }
        }
    }
}

@Composable
private fun EmptyFeedPlaceholder(tab: FeedTab) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "✨",
                fontSize = 32.sp
            )
            Text(
                text = when (tab) {
                    FeedTab.FOLLOWING -> "No posts from followed creators yet"
                    FeedTab.MUTUALS -> "No posts from mutual connections yet"
                    else -> "No posts available"
                },
                fontWeight = FontWeight.SemiBold,
                color = ElvoraSilverLight,
                fontSize = 15.sp
            )
            Text(
                text = "Discover new creators or follow mutual friends to populate your feed!",
                color = ElvoraSilverMuted,
                fontSize = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
