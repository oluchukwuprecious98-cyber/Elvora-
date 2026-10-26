package com.example.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.User
import com.example.ui.theme.ElvoraBorder
import com.example.ui.theme.ElvoraCyanAccent
import com.example.ui.theme.ElvoraElectricViolet
import com.example.ui.theme.ElvoraMidnight
import com.example.ui.theme.ElvoraSilverLight
import com.example.ui.theme.ElvoraSilverMuted
import com.example.ui.theme.ElvoraSilverPure
import com.example.ui.theme.ElvoraSurfaceCard
import com.example.ui.theme.ElvoraSurfaceDark

@Composable
fun MutualsSheet(
    users: List<User>,
    onFollowToggle: (String) -> Unit,
    onClose: () -> Unit
) {
    var search by remember { mutableStateOf("") }
    val filtered = remember(users, search) {
        if (search.isBlank()) users
        else users.filter { it.name.contains(search, true) || it.username.contains(search, true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ElvoraMidnight)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = ElvoraSilverPure
                )
            }
            Text(
                text = "Mutual Connections ⚡",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = ElvoraSilverPure
            )
            Spacer(modifier = Modifier.size(48.dp))
        }

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            placeholder = { Text("Search mutual friends...", color = ElvoraSilverMuted, fontSize = 13.sp) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = ElvoraSilverMuted, modifier = Modifier.size(20.dp))
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = ElvoraSurfaceDark,
                unfocusedContainerColor = ElvoraSurfaceDark,
                focusedBorderColor = ElvoraElectricViolet,
                unfocusedBorderColor = ElvoraBorder,
                focusedTextColor = ElvoraSilverPure,
                unfocusedTextColor = ElvoraSilverPure
            )
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filtered, key = { it.id }) { user ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(ElvoraSurfaceDark)
                        .border(1.dp, ElvoraBorder, RoundedCornerShape(14.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        AsyncImage(
                            model = user.avatarUrl,
                            contentDescription = user.name,
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, ElvoraElectricViolet, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = user.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElvoraSilverPure
                                )
                                if (user.isVerified) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Verified",
                                        tint = ElvoraElectricViolet,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                            Text(
                                text = "@${user.username}",
                                fontSize = 12.sp,
                                color = ElvoraSilverMuted
                            )
                            Text(
                                text = user.mutualWithText.ifEmpty { "${user.mutualFriendsCount} mutuals" },
                                fontSize = 10.sp,
                                color = ElvoraCyanAccent
                            )
                        }
                    }

                    Button(
                        onClick = { onFollowToggle(user.id) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (user.isFollowing) ElvoraSurfaceCard else ElvoraElectricViolet,
                            contentColor = ElvoraSilverPure
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = if (user.isFollowing) "Connected" else "+ Add",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
