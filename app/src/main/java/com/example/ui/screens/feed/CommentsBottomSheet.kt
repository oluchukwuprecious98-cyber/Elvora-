package com.example.ui.screens.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Comment
import com.example.data.model.Post
import com.example.ui.theme.ElvoraBorder
import com.example.ui.theme.ElvoraElectricViolet
import com.example.ui.theme.ElvoraMidnight
import com.example.ui.theme.ElvoraRoseAccent
import com.example.ui.theme.ElvoraSilverLight
import com.example.ui.theme.ElvoraSilverMid
import com.example.ui.theme.ElvoraSilverMuted
import com.example.ui.theme.ElvoraSilverPure
import com.example.ui.theme.ElvoraSurfaceCard
import com.example.ui.theme.ElvoraSurfaceDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
    post: Post,
    comments: List<Comment>,
    currentUserAvatar: String,
    onAddComment: (String) -> Unit,
    onLikeComment: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var newCommentText by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ElvoraMidnight,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .navigationBarsPadding()
                .imePadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Comments (${comments.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElvoraSilverPure
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = ElvoraSilverLight
                    )
                }
            }

            HorizontalDivider(color = ElvoraBorder.copy(alpha = 0.5f), thickness = 0.5.dp)

            // Comments List
            if (comments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No comments yet. Start the conversation! 💬",
                        color = ElvoraSilverMuted,
                        fontSize = 13.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(comments, key = { it.id }) { comment ->
                        CommentRow(
                            comment = comment,
                            onLike = { onLikeComment(comment.id) }
                        )
                    }
                }
            }

            HorizontalDivider(color = ElvoraBorder.copy(alpha = 0.5f), thickness = 0.5.dp)

            // Add Comment Input Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AsyncImage(
                    model = currentUserAvatar,
                    contentDescription = "You",
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .border(1.dp, ElvoraElectricViolet, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(ElvoraSurfaceDark)
                        .border(1.dp, ElvoraBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    BasicTextField(
                        value = newCommentText,
                        onValueChange = { newCommentText = it },
                        textStyle = TextStyle(color = ElvoraSilverPure, fontSize = 13.sp),
                        cursorBrush = SolidColor(ElvoraElectricViolet),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            if (newCommentText.isEmpty()) {
                                Text(
                                    text = "Add a comment for ${post.author.username}...",
                                    color = ElvoraSilverMuted,
                                    fontSize = 13.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                }

                Text(
                    text = "Post",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (newCommentText.isNotBlank()) ElvoraElectricViolet else ElvoraSilverMuted,
                    modifier = Modifier.clickable(enabled = newCommentText.isNotBlank()) {
                        onAddComment(newCommentText)
                        newCommentText = ""
                    }
                )
            }
        }
    }
}

@Composable
private fun CommentRow(
    comment: Comment,
    onLike: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AsyncImage(
                model = comment.author.avatarUrl,
                contentDescription = comment.author.name,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .border(1.dp, ElvoraBorder, CircleShape),
                contentScale = ContentScale.Crop
            )

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = comment.author.username,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElvoraSilverPure
                    )
                    Text(
                        text = comment.timeAgo,
                        fontSize = 10.sp,
                        color = ElvoraSilverMuted
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = comment.text,
                    fontSize = 13.sp,
                    color = ElvoraSilverLight,
                    lineHeight = 17.sp
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable(onClick = onLike)
        ) {
            Icon(
                imageVector = if (comment.isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like comment",
                tint = if (comment.isLiked) ElvoraRoseAccent else ElvoraSilverMuted,
                modifier = Modifier.size(16.dp)
            )
            if (comment.likesCount > 0) {
                Text(
                    text = comment.likesCount.toString(),
                    fontSize = 10.sp,
                    color = ElvoraSilverMuted
                )
            }
        }
    }
}
