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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
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
import com.example.ui.theme.ElvoraElectricViolet
import com.example.ui.theme.ElvoraMidnight
import com.example.ui.theme.ElvoraSilverLight
import com.example.ui.theme.ElvoraSilverMuted
import com.example.ui.theme.ElvoraSilverPure
import com.example.ui.theme.ElvoraSurfaceDark

@Composable
fun EditProfileModal(
    currentUser: User,
    onClose: () -> Unit,
    onSave: (name: String, username: String, bio: String, avatarUrl: String) -> Unit
) {
    var name by remember { mutableStateOf(currentUser.name) }
    var username by remember { mutableStateOf(currentUser.username) }
    var bio by remember { mutableStateOf(currentUser.bio) }
    var avatarUrl by remember { mutableStateOf(currentUser.avatarUrl) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ElvoraMidnight)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Header
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
                    contentDescription = "Cancel",
                    tint = ElvoraSilverPure
                )
            }

            Text(
                text = "Edit Profile",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = ElvoraSilverPure
            )

            Button(
                onClick = { onSave(name, username, bio, avatarUrl) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ElvoraElectricViolet,
                    contentColor = ElvoraSilverPure
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Done", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        // Form fields
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Avatar with change photo action
            Box(contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = name,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .border(2.dp, ElvoraElectricViolet, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(ElvoraElectricViolet)
                        .border(2.dp, ElvoraMidnight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Change photo",
                        tint = ElvoraSilverPure,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Text(
                text = "Change Profile Photo",
                fontSize = 13.sp,
                color = ElvoraElectricViolet,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    // Quick avatar presets toggle
                    avatarUrl = if (avatarUrl.contains("534528741775")) {
                        "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?auto=format&fit=crop&w=400&q=80"
                    } else {
                        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80"
                    }
                }
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Display Name", color = ElvoraSilverMuted) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
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

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username", color = ElvoraSilverMuted) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
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

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio", color = ElvoraSilverMuted) },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth(),
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
        }
    }
}
