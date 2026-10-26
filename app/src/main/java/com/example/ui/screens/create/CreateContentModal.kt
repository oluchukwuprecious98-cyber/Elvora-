package com.example.ui.screens.create

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
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

enum class CreateMode {
    POST,
    REEL,
    MOMENT
}

@Composable
fun CreateContentModal(
    onClose: () -> Unit,
    onCreatePost: (caption: String, hashtags: List<String>, audioTrack: String?, location: String?, mediaRes: Int?, mediaUrl: String?) -> Unit,
    onCreateMoment: (caption: String, bgGradient: List<Long>, mediaRes: Int?, mediaUrl: String?) -> Unit,
    onCreateReel: (caption: String, audioTrack: String, mediaRes: Int?) -> Unit
) {
    var mode by remember { mutableStateOf(CreateMode.POST) }
    var captionText by remember { mutableStateOf("") }
    var locationText by remember { mutableStateOf("Neo-Tokyo Studio") }
    var audioTrackText by remember { mutableStateOf("Violet Echoes • Original Mix") }
    var selectedMediaRes by remember { mutableStateOf<Int?>(R.drawable.elvora_post_art_1793042399481) }
    var selectedMediaUri by remember { mutableStateOf<String?>(null) }
    var selectedGradientIndex by remember { mutableStateOf(0) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedMediaUri = uri.toString()
            selectedMediaRes = null
        }
    }

    val gradientPresets = listOf(
        listOf(0xFF0F0C20, 0xFF4C1D95),
        listOf(0xFF1E1B4B, 0xFF7C3AED),
        listOf(0xFF022C22, 0xFF0D9488),
        listOf(0xFF3B0764, 0xFFBE185D)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ElvoraMidnight)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("create_modal_screen")
    ) {
        // Modal Header
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
                text = when (mode) {
                    CreateMode.POST -> "Create Post"
                    CreateMode.REEL -> "Upload Reel"
                    CreateMode.MOMENT -> "Add 24h Moment"
                },
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = ElvoraSilverPure
            )

            // Publish Button
            Button(
                onClick = {
                    when (mode) {
                        CreateMode.POST -> {
                            val tags = captionText.split(" ", "\n")
                                .filter { it.startsWith("#") }
                                .map { it.removePrefix("#") }
                            onCreatePost(
                                captionText.ifBlank { "Sharing a new vision on Elvora ⚡" },
                                tags.ifEmpty { listOf("elvora", "creative") },
                                audioTrackText,
                                locationText,
                                selectedMediaRes,
                                selectedMediaUri
                            )
                        }
                        CreateMode.REEL -> {
                            onCreateReel(
                                captionText.ifBlank { "New Reel on Elvora 🎬✨ #reels #elvora" },
                                audioTrackText,
                                selectedMediaRes ?: R.drawable.elvora_reel_cover_1793042413260
                            )
                        }
                        CreateMode.MOMENT -> {
                            onCreateMoment(
                                captionText.ifBlank { "Living the midnight vibe 💜" },
                                gradientPresets[selectedGradientIndex],
                                selectedMediaRes,
                                selectedMediaUri
                            )
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ElvoraElectricViolet,
                    contentColor = ElvoraSilverPure
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Share",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        // Mode Selector: Post, Reel, Moment
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CreateModeTab(
                label = "📸 Post",
                isSelected = mode == CreateMode.POST,
                onClick = {
                    mode = CreateMode.POST
                    selectedMediaRes = R.drawable.elvora_post_art_1793042399481
                }
            )
            CreateModeTab(
                label = "🎬 Reel",
                isSelected = mode == CreateMode.REEL,
                onClick = {
                    mode = CreateMode.REEL
                    selectedMediaRes = R.drawable.elvora_reel_cover_1793042413260
                }
            )
            CreateModeTab(
                label = "⏳ 24h Moment",
                isSelected = mode == CreateMode.MOMENT,
                onClick = {
                    mode = CreateMode.MOMENT
                }
            )
        }

        // Scrollable Form
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Media Preview Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(if (mode == CreateMode.REEL) 1.2f else 1.3f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (mode == CreateMode.MOMENT && selectedMediaRes == null && selectedMediaUri == null) {
                            Brush.verticalGradient(gradientPresets[selectedGradientIndex].map { Color(it) })
                        } else {
                            Brush.linearGradient(listOf(ElvoraSurfaceDark, ElvoraSurfaceDark))
                        }
                    )
                    .border(1.dp, ElvoraBorder, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (selectedMediaUri != null) {
                    AsyncImage(
                        model = selectedMediaUri,
                        contentDescription = "Selected media",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else if (selectedMediaRes != null) {
                    Image(
                        painter = painterResource(id = selectedMediaRes!!),
                        contentDescription = "Selected media",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else if (mode == CreateMode.MOMENT) {
                    Text(
                        text = captionText.ifEmpty { "Type your 24h thought..." },
                        color = ElvoraSilverPure,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(24.dp)
                    )
                }

                // Choose photo / camera button
                Button(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ElvoraMidnight.copy(alpha = 0.8f),
                        contentColor = ElvoraSilverPure
                    ),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElvoraBorderLight)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Select photo",
                            tint = ElvoraElectricViolet,
                            modifier = Modifier.size(16.dp)
                        )
                        Text("Gallery", fontSize = 12.sp)
                    }
                }
            }

            // Moment 24h Countdown Alert
            if (mode == CreateMode.MOMENT) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ElvoraSurfaceCard)
                        .border(1.dp, ElvoraElectricViolet.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Timer",
                        tint = ElvoraElectricViolet,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Elvora Moments automatically disappear after 24 hours.",
                        fontSize = 11.sp,
                        color = ElvoraSilverLight
                    )
                }

                // Gradient selector for text stories
                Text(
                    text = "Background Gradient",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElvoraSilverMid
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    gradientPresets.forEachIndexed { index, colors ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(colors.map { Color(it) }))
                                .border(
                                    2.dp,
                                    if (selectedGradientIndex == index) ElvoraSilverPure else Color.Transparent,
                                    CircleShape
                                )
                                .clickable {
                                    selectedGradientIndex = index
                                    selectedMediaRes = null
                                    selectedMediaUri = null
                                }
                        )
                    }
                }
            }

            // Caption Field
            OutlinedTextField(
                value = captionText,
                onValueChange = { captionText = it },
                label = { Text("Write a caption & #hashtags...", color = ElvoraSilverMuted, fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
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

            // Audio soundtrack selector
            OutlinedTextField(
                value = audioTrackText,
                onValueChange = { audioTrackText = it },
                label = { Text("Soundtrack / Audio", color = ElvoraSilverMuted, fontSize = 12.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "Audio track",
                        tint = ElvoraElectricViolet,
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
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

            // Location field (for posts)
            if (mode == CreateMode.POST) {
                OutlinedTextField(
                    value = locationText,
                    onValueChange = { locationText = it },
                    label = { Text("Location Tag", color = ElvoraSilverMuted, fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
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
}

@Composable
private fun CreateModeTab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) ElvoraElectricViolet else ElvoraSurfaceDark
            )
            .border(
                1.dp,
                if (isSelected) ElvoraSilverPure.copy(alpha = 0.3f) else ElvoraBorder,
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) ElvoraSilverPure else ElvoraSilverMuted
        )
    }
}
