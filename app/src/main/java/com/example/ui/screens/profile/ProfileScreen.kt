package com.example.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.ElvoraBorder
import com.example.ui.theme.ElvoraBorderLight
import com.example.ui.theme.ElvoraCyanAccent
import com.example.ui.theme.ElvoraElectricViolet
import com.example.ui.theme.ElvoraMidnight
import com.example.ui.theme.ElvoraSilverLight
import com.example.ui.theme.ElvoraSilverMid
import com.example.ui.theme.ElvoraSilverMuted
import com.example.ui.theme.ElvoraSilverPure
import com.example.ui.theme.ElvoraSurfaceCard
import com.example.ui.theme.ElvoraSurfaceDark
import com.example.ui.theme.ElvoraVioletBright
import com.example.ui.theme.ElvoraVioletGlow
import com.example.ui.viewmodel.ProfileTab

@Composable
fun ProfileScreen(
    user: User,
    posts: List<Post>,
    reels: List<Reel>,
    selectedTab: ProfileTab,
    onTabSelected: (ProfileTab) -> Unit,
    onEditProfileClick: () -> Unit,
    onMutualsClick: () -> Unit,
    onShareProfileClick: () -> Unit,
    onPostClick: (Post) -> Unit,
    modifier: Modifier = Modifier
) {
    val myPosts = posts.filter { it.author.id == user.id || it.author.id == "current_user" }
    val savedPosts = posts.filter { it.isSaved }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ElvoraMidnight)
            .statusBarsPadding()
            .testTag("profile_screen")
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = user.username,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElvoraSilverPure
                    )
                    if (user.isVerified) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = ElvoraElectricViolet,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onShareProfileClick) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Profile",
                            tint = ElvoraSilverLight,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(onClick = onEditProfileClick) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = ElvoraSilverLight,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Avatar + Stats Section
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Profile Avatar with Glowing Border & Camera Icon
                Box(contentAlignment = Alignment.BottomEnd) {
                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = user.name,
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .border(
                                2.dp,
                                Brush.linearGradient(
                                    listOf(ElvoraElectricViolet, ElvoraVioletGlow, ElvoraSilverPure)
                                ),
                                CircleShape
                            )
                            .clickable(onClick = onEditProfileClick),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(ElvoraElectricViolet)
                            .border(1.5.dp, ElvoraMidnight, CircleShape)
                            .clickable(onClick = onEditProfileClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Edit photo",
                            tint = ElvoraSilverPure,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                // Stats Row: Posts, Followers, Following, Mutuals
                Row(
                    modifier = Modifier.padding(start = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatItem(number = "${myPosts.size + 4}", label = "Posts")
                    StatItem(number = "4.8K", label = "Followers")
                    StatItem(number = "${user.followingCount}", label = "Following")
                    StatItem(
                        number = "${user.mutualFriendsCount}",
                        label = "Mutuals ⚡",
                        isClickable = true,
                        onClick = onMutualsClick
                    )
                }
            }
        }

        // Bio & Vibe Tags
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = user.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElvoraSilverPure
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = user.bio,
                    fontSize = 13.sp,
                    color = ElvoraSilverLight,
                    lineHeight = 18.sp
                )

                // Vibe tags
                if (user.vibeTags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(user.vibeTags) { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ElvoraSurfaceCard)
                                    .border(0.5.dp, ElvoraBorder, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "#$tag",
                                    fontSize = 11.sp,
                                    color = ElvoraVioletGlow,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // Profile Action Buttons: Edit Profile, Share, Mutuals
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onEditProfileClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ElvoraSurfaceCard,
                        contentColor = ElvoraSilverPure
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElvoraBorderLight)
                ) {
                    Text("Edit Profile", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onShareProfileClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ElvoraSurfaceCard,
                        contentColor = ElvoraSilverPure
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElvoraBorderLight)
                ) {
                    Text("Share Profile", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onMutualsClick,
                    modifier = Modifier.weight(0.9f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ElvoraElectricViolet,
                        contentColor = ElvoraSilverPure
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Mutuals ⚡", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Story Highlights Carousel
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                val highlights = listOf(
                    "Vibes ✨" to "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=200&q=80",
                    "Reels 🎬" to "https://images.unsplash.com/photo-1574717024653-61fd2cf4d44d?auto=format&fit=crop&w=200&q=80",
                    "Tokyo 🗼" to "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=200&q=80",
                    "Studio 🎹" to "https://images.unsplash.com/photo-1598488035139-bdbb2231ce04?auto=format&fit=crop&w=200&q=80"
                )
                items(highlights) { (title, url) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(62.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, ElvoraBorder, CircleShape)
                                .padding(3.dp)
                        ) {
                            AsyncImage(
                                model = url,
                                contentDescription = title,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = title,
                            fontSize = 11.sp,
                            color = ElvoraSilverLight,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            HorizontalDivider(
                color = ElvoraBorder.copy(alpha = 0.5f),
                thickness = 0.5.dp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        // Tabs: Posts, Reels, Saved
        item {
            TabRow(
                selectedTabIndex = selectedTab.ordinal,
                containerColor = ElvoraMidnight,
                contentColor = ElvoraElectricViolet,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                        color = ElvoraElectricViolet
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == ProfileTab.POSTS,
                    onClick = { onTabSelected(ProfileTab.POSTS) },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == ProfileTab.POSTS) Icons.Filled.GridOn else Icons.Outlined.GridOn,
                            contentDescription = "Posts",
                            tint = if (selectedTab == ProfileTab.POSTS) ElvoraSilverPure else ElvoraSilverMuted
                        )
                    }
                )
                Tab(
                    selected = selectedTab == ProfileTab.REELS,
                    onClick = { onTabSelected(ProfileTab.REELS) },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == ProfileTab.REELS) Icons.Filled.PlayArrow else Icons.Outlined.PlayCircleOutline,
                            contentDescription = "Reels",
                            tint = if (selectedTab == ProfileTab.REELS) ElvoraSilverPure else ElvoraSilverMuted
                        )
                    }
                )
                Tab(
                    selected = selectedTab == ProfileTab.SAVED,
                    onClick = { onTabSelected(ProfileTab.SAVED) },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == ProfileTab.SAVED) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Saved",
                            tint = if (selectedTab == ProfileTab.SAVED) ElvoraSilverPure else ElvoraSilverMuted
                        )
                    }
                )
            }
        }

        // Content Grid according to selected tab
        item {
            val displayList = when (selectedTab) {
                ProfileTab.POSTS -> myPosts.ifEmpty { posts }
                ProfileTab.REELS -> posts.take(2)
                ProfileTab.SAVED -> savedPosts.ifEmpty { posts.take(1) }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                val chunked = displayList.chunked(3)
                chunked.forEach { rowPosts ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        rowPosts.forEach { post ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .background(ElvoraSurfaceDark)
                                    .clickable { onPostClick(post) }
                            ) {
                                if (post.mediaRes != null) {
                                    Image(
                                        painter = painterResource(id = post.mediaRes),
                                        contentDescription = "Post thumbnail",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else if (post.mediaUrl != null) {
                                    AsyncImage(
                                        model = post.mediaUrl,
                                        contentDescription = "Post thumbnail",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                        // Fill remaining spaces in row
                        repeat(3 - rowPosts.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
private fun StatItem(
    number: String,
    label: String,
    isClickable: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(enabled = isClickable, onClick = onClick)
    ) {
        Text(
            text = number,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = if (isClickable) ElvoraVioletGlow else ElvoraSilverPure
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = if (isClickable) ElvoraCyanAccent else ElvoraSilverMuted
        )
    }
}
