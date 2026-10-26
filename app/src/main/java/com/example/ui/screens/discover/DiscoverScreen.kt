package com.example.ui.screens.discover

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Icon
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
import com.example.ui.theme.ElvoraVioletGlow

@Composable
fun DiscoverScreen(
    suggestedUsers: List<User>,
    posts: List<Post>,
    onFollowUser: (String) -> Unit,
    onPostClick: (Post) -> Unit,
    onUserClick: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf("All", "⚡ Cyber", "🎧 Music", "👗 Futurism", "🌆 Tokyo", "🎨 3D Art")

    val filteredUsers = remember(suggestedUsers, searchQuery) {
        if (searchQuery.isBlank()) suggestedUsers
        else suggestedUsers.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.username.contains(searchQuery, ignoreCase = true) ||
            it.bio.contains(searchQuery, ignoreCase = true)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ElvoraMidnight)
            .statusBarsPadding()
            .testTag("discover_screen")
    ) {
        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text("Search creators, tags, and music...", color = ElvoraSilverMuted, fontSize = 13.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = ElvoraElectricViolet,
                        modifier = Modifier.size(20.dp)
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
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
        }

        // Category Chips
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = cat == selectedCategory
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
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = cat,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) ElvoraSilverPure else ElvoraSilverMuted
                        )
                    }
                }
            }
        }

        // Suggested Mutual Friends Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Whatshot,
                            contentDescription = "Trending",
                            tint = ElvoraCyanAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Find Friends & Mutual Connections",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElvoraSilverPure
                        )
                    }
                }

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredUsers, key = { it.id }) { user ->
                        MutualFriendCard(
                            user = user,
                            onFollow = { onFollowUser(user.id) },
                            onClick = { onUserClick(user) }
                        )
                    }
                }
            }
        }

        // Explore Grid Header
        item {
            Text(
                text = "Trending on Elvora",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ElvoraSilverPure,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )
        }

        // Asymmetric Explore Grid
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Row 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    posts.getOrNull(0)?.let { p ->
                        ExploreGridTile(
                            post = p,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            onClick = { onPostClick(p) }
                        )
                    }
                    posts.getOrNull(1)?.let { p ->
                        ExploreGridTile(
                            post = p,
                            isReel = true,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            onClick = { onPostClick(p) }
                        )
                    }
                }

                // Row 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    posts.getOrNull(2)?.let { p ->
                        ExploreGridTile(
                            post = p,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            onClick = { onPostClick(p) }
                        )
                    }
                    // Additional creative tile
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(ElvoraElectricViolet, ElvoraMidnight)
                                )
                            )
                            .border(1.dp, ElvoraBorder, RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "⚡", fontSize = 24.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "#Tokyo2049",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = ElvoraSilverPure
                            )
                            Text(
                                text = "42.8K Posts",
                                fontSize = 11.sp,
                                color = ElvoraSilverMid
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun MutualFriendCard(
    user: User,
    onFollow: () -> Unit,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(148.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(ElvoraSurfaceDark)
            .border(1.dp, ElvoraBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = user.avatarUrl,
            contentDescription = user.name,
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .border(1.5.dp, ElvoraElectricViolet, CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = user.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ElvoraSilverPure,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (user.isVerified) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Verified",
                    tint = ElvoraElectricViolet,
                    modifier = Modifier.size(11.dp)
                )
            }
        }

        Text(
            text = "@${user.username}",
            fontSize = 11.sp,
            color = ElvoraSilverMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = user.mutualWithText.ifEmpty { "${user.mutualFriendsCount} mutual friends" },
            fontSize = 9.sp,
            color = ElvoraCyanAccent,
            maxLines = 2,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 12.sp,
            modifier = Modifier.height(26.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (user.isFollowing) ElvoraSurfaceCard else ElvoraElectricViolet
                )
                .border(
                    1.dp,
                    if (user.isFollowing) ElvoraBorder else ElvoraSilverPure.copy(alpha = 0.2f),
                    RoundedCornerShape(12.dp)
                )
                .clickable(onClick = onFollow)
                .padding(vertical = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (user.isFollowing) "Following" else "+ Follow",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ElvoraSilverPure
            )
        }
    }
}

@Composable
private fun ExploreGridTile(
    post: Post,
    isReel: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(ElvoraSurfaceCard)
            .border(1.dp, ElvoraBorder, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        if (post.mediaRes != null) {
            Image(
                painter = painterResource(id = post.mediaRes),
                contentDescription = "Explore media",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else if (post.mediaUrl != null) {
            AsyncImage(
                model = post.mediaUrl,
                contentDescription = "Explore media",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        if (isReel) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(ElvoraMidnight.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Reel",
                    tint = ElvoraSilverPure,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
