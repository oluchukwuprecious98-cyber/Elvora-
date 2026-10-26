package com.example.ui.screens.moments

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.data.model.Moment
import com.example.data.model.User
import com.example.ui.components.StoryRingAvatar
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
import com.example.ui.theme.ElvoraVioletBright
import com.example.ui.theme.ElvoraVioletGlow

@Composable
fun MomentsScreen(
    currentUser: User,
    moments: List<Moment>,
    onMomentClick: (Moment) -> Unit,
    onAddMomentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val myMoment = moments.find { it.author.id == currentUser.id || it.author.id == "current_user" }
    val friendMoments = moments.filter { it.author.id != currentUser.id && it.author.id != "current_user" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ElvoraMidnight)
            .statusBarsPadding()
            .testTag("moments_screen")
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Elvora Moments",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ElvoraSilverPure
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ElvoraElectricViolet.copy(alpha = 0.2f))
                            .border(1.dp, ElvoraElectricViolet, RoundedCornerShape(8.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "24h Expire",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElvoraVioletGlow
                        )
                    }
                }

                Button(
                    onClick = onAddMomentClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ElvoraElectricViolet,
                        contentColor = ElvoraSilverPure
                    ),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Moment",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Post", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Your Moment Highlight Banner
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Your Status",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElvoraSilverMid
                )

                Spacer(modifier = Modifier.height(6.dp))

                if (myMoment != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(ElvoraSurfaceDark)
                            .border(1.dp, ElvoraElectricViolet, RoundedCornerShape(16.dp))
                            .clickable { onMomentClick(myMoment) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StoryRingAvatar(
                                avatarUrl = currentUser.avatarUrl,
                                hasStory = true,
                                isViewed = false,
                                isCurrentUser = false,
                                size = 52.dp,
                                onClick = { onMomentClick(myMoment) }
                            )

                            Column {
                                Text(
                                    text = "Your 24h Moment is Live",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElvoraSilverPure
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = "Timer",
                                        tint = ElvoraCyanAccent,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "${myMoment.postedTimeAgo} • Disappears in ${myMoment.hoursRemaining}h",
                                        fontSize = 11.sp,
                                        color = ElvoraSilverMuted
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = onAddMomentClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ElvoraSurfaceCard,
                                contentColor = ElvoraSilverPure
                            ),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ElvoraBorder)
                        ) {
                            Text(text = "+ New", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(ElvoraSurfaceDark)
                            .border(1.dp, ElvoraBorder, RoundedCornerShape(16.dp))
                            .clickable(onClick = onAddMomentClick)
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StoryRingAvatar(
                            avatarUrl = currentUser.avatarUrl,
                            hasStory = false,
                            isCurrentUser = true,
                            size = 50.dp,
                            onClick = onAddMomentClick
                        )

                        Column {
                            Text(
                                text = "Share a 24-hour Moment",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ElvoraSilverPure
                            )
                            Text(
                                text = "Photos, videos, or quick thoughts that disappear after 24 hours.",
                                fontSize = 11.sp,
                                color = ElvoraSilverMuted
                            )
                        }
                    }
                }
            }
        }

        // Friends' Moments Section Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Active Stories (${friendMoments.size})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElvoraSilverPure
                )
                Text(
                    text = "Tap to view full screen",
                    fontSize = 11.sp,
                    color = ElvoraSilverMuted
                )
            }
        }

        // Active Moments 2-Column Grid
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val chunked = friendMoments.chunked(2)
                chunked.forEach { rowMoments ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowMoments.forEach { moment ->
                            MomentCard(
                                moment = moment,
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(0.78f),
                                onClick = { onMomentClick(moment) }
                            )
                        }
                        if (rowMoments.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // Story Highlights Archive Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                color = ElvoraBorder.copy(alpha = 0.5f),
                thickness = 0.5.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Archive",
                    tint = ElvoraElectricViolet,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Moments Archive & Highlights",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElvoraSilverPure
                )
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val archives = listOf(
                    "Tokyo Club" to "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=300&q=80",
                    "Synthesizers" to "https://images.unsplash.com/photo-1598488035139-bdbb2231ce04?auto=format&fit=crop&w=300&q=80",
                    "Neon Runway" to "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=300&q=80",
                    "3D Renders" to "https://images.unsplash.com/photo-1574717024653-61fd2cf4d44d?auto=format&fit=crop&w=300&q=80"
                )
                items(archives) { (name, url) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(72.dp)
                    ) {
                        AsyncImage(
                            model = url,
                            contentDescription = name,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, ElvoraBorder, RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = name,
                            fontSize = 11.sp,
                            color = ElvoraSilverLight,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
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
private fun MomentCard(
    moment: Moment,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val gradientBrush = remember(moment.bgGradient) {
        Brush.verticalGradient(moment.bgGradient.map { Color(it) })
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(ElvoraSurfaceDark)
            .border(
                1.5.dp,
                if (!moment.isViewed) ElvoraElectricViolet else ElvoraBorder,
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
    ) {
        // Media or gradient
        if (moment.mediaRes != null) {
            Image(
                painter = painterResource(id = moment.mediaRes),
                contentDescription = "Moment thumbnail",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else if (moment.mediaUrl != null) {
            AsyncImage(
                model = moment.mediaUrl,
                contentDescription = "Moment thumbnail",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradientBrush)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = moment.textCaption,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElvoraSilverPure,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        // Dark gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            ElvoraMidnight.copy(alpha = 0.5f),
                            Color.Transparent,
                            ElvoraMidnight.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // Top Author Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            AsyncImage(
                model = moment.author.avatarUrl,
                contentDescription = moment.author.name,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .border(1.dp, ElvoraElectricViolet, CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(
                text = moment.author.name.substringBefore(" "),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ElvoraSilverPure,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Bottom Expiration Timer Badge
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(ElvoraMidnight.copy(alpha = 0.7f))
                .padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = "Remaining",
                tint = ElvoraElectricViolet,
                modifier = Modifier.size(11.dp)
            )
            Text(
                text = "${moment.hoursRemaining}h left",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = ElvoraSilverPure
            )
        }
    }
}
