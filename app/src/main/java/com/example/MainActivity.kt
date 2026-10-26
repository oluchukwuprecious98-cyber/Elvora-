package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.components.ElvoraBottomNav
import com.example.ui.components.ElvoraTopBar
import com.example.ui.navigation.Screen
import com.example.ui.screens.chat.ChatConversationScreen
import com.example.ui.screens.chat.ChatListScreen
import com.example.ui.screens.create.CreateContentModal
import com.example.ui.screens.discover.DiscoverScreen
import com.example.ui.screens.feed.CommentsBottomSheet
import com.example.ui.screens.feed.HomeScreen
import com.example.ui.screens.feed.ShareBottomSheet
import com.example.ui.screens.moments.MomentViewerScreen
import com.example.ui.screens.moments.MomentsScreen
import com.example.ui.screens.notifications.NotificationsScreen
import com.example.ui.screens.profile.EditProfileModal
import com.example.ui.screens.profile.MutualsSheet
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.reels.ReelsScreen
import com.example.ui.theme.ElvoraBorder
import com.example.ui.theme.ElvoraElectricViolet
import com.example.ui.theme.ElvoraMidnight
import com.example.ui.theme.ElvoraSilverPure
import com.example.ui.theme.ElvoraSurfaceDark
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ElvoraViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ElvoraAppRoot()
            }
        }
    }
}

@Composable
fun ElvoraAppRoot(
    viewModel: ElvoraViewModel = viewModel()
) {
    val context = LocalContext.current
    val navController = rememberNavController()

    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val suggestedUsers by viewModel.suggestedUsers.collectAsStateWithLifecycle()
    val moments by viewModel.moments.collectAsStateWithLifecycle()
    val posts by viewModel.posts.collectAsStateWithLifecycle()
    val filteredPosts by viewModel.filteredPosts.collectAsStateWithLifecycle()
    val commentsMap by viewModel.comments.collectAsStateWithLifecycle()
    val reels by viewModel.reels.collectAsStateWithLifecycle()
    val conversations by viewModel.conversations.collectAsStateWithLifecycle()
    val messagesMap by viewModel.messages.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()

    val feedTab by viewModel.feedTab.collectAsStateWithLifecycle()
    val profileTab by viewModel.profileTab.collectAsStateWithLifecycle()

    val activeMoment by viewModel.activeMoment.collectAsStateWithLifecycle()
    val activeCommentPost by viewModel.activeCommentPost.collectAsStateWithLifecycle()
    val activeSharePost by viewModel.activeSharePost.collectAsStateWithLifecycle()
    val isCreateOpen by viewModel.isCreateOpen.collectAsStateWithLifecycle()
    val isEditProfileOpen by viewModel.isEditProfileOpen.collectAsStateWithLifecycle()
    val isMutualsSheetOpen by viewModel.isMutualsSheetOpen.collectAsStateWithLifecycle()
    val snackMessage by viewModel.snackMessage.collectAsStateWithLifecycle()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val unreadChatCount = conversations.sumOf { it.unreadCount }
    val hasUnreadNotifs = notifications.any { !it.isRead }

    // Hide bottom navigation on full-screen detail routes or overlays
    val isChatDetail = currentRoute?.startsWith("chat_detail") == true
    val showBottomBar = activeMoment == null && !isCreateOpen && !isEditProfileOpen && !isMutualsSheetOpen && !isChatDetail

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ElvoraMidnight)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = ElvoraMidnight,
            topBar = {
                if (currentRoute == Screen.Home.route && activeMoment == null) {
                    ElvoraTopBar(
                        hasUnreadNotifications = hasUnreadNotifs,
                        onCreateClick = { viewModel.openCreate() },
                        onDiscoverClick = { navController.navigate(Screen.Discover.route) },
                        onNotificationsClick = { navController.navigate(Screen.Notifications.route) }
                    )
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    ElvoraBottomNav(
                        currentRoute = currentRoute,
                        userAvatarUrl = currentUser.avatarUrl,
                        unreadChatCount = unreadChatCount,
                        onNavigate = { targetRoute ->
                            navController.navigate(targetRoute) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // 1. Home Feed
                composable(Screen.Home.route) {
                    HomeScreen(
                        currentUser = currentUser,
                        moments = moments,
                        posts = filteredPosts,
                        currentTab = feedTab,
                        onTabSelected = { viewModel.setFeedTab(it) },
                        onMomentClick = { viewModel.openMoment(it) },
                        onAddMomentClick = { viewModel.openCreate() },
                        onLikePost = { viewModel.toggleLikePost(it) },
                        onCommentClick = { viewModel.openComments(it) },
                        onShareClick = { viewModel.openShare(it) },
                        onSaveClick = { viewModel.toggleSavePost(it) },
                        onRepostClick = { viewModel.repostPost(it) }
                    )
                }

                // 2. Reels
                composable(Screen.Reels.route) {
                    ReelsScreen(
                        reels = reels,
                        onLikeReel = { viewModel.toggleLikeReel(it) },
                        onSaveReel = { viewModel.toggleSaveReel(it) },
                        onFollowCreator = { viewModel.toggleFollowReelCreator(it) },
                        onCommentClick = { viewModel.openComments(it) },
                        onShareClick = { viewModel.openShare(it) },
                        onCreateReelClick = { viewModel.openCreate() }
                    )
                }

                // 3. Moments (24-Hour Stories Screen)
                composable(Screen.Moments.route) {
                    MomentsScreen(
                        currentUser = currentUser,
                        moments = moments,
                        onMomentClick = { viewModel.openMoment(it) },
                        onAddMomentClick = { viewModel.openCreate() }
                    )
                }

                // 4. Chat List
                composable(Screen.Chat.route) {
                    ChatListScreen(
                        conversations = conversations,
                        onConversationClick = { conv ->
                            navController.navigate(Screen.ChatDetail.createRoute(conv.id))
                        },
                        onBack = { navController.popBackStack() }
                    )
                }

                // Chat Conversation Detail
                composable(Screen.ChatDetail.route) { backStackEntry ->
                    val conversationId = backStackEntry.arguments?.getString("conversationId")
                    val conversation = conversations.find { it.id == conversationId } ?: conversations.first()
                    val messages = messagesMap[conversation.id] ?: emptyList()

                    ChatConversationScreen(
                        conversation = conversation,
                        messages = messages,
                        onSendMessage = { text -> viewModel.sendChatMessage(conversation.id, text) },
                        onSendVoiceNote = { sec -> viewModel.sendVoiceNote(conversation.id, sec) },
                        onReactMessage = { msgId, emoji -> viewModel.reactToMessage(conversation.id, msgId, emoji) },
                        onBack = { navController.popBackStack() },
                        onShowSnack = { viewModel.showSnack(it) }
                    )
                }

                // 5. Profile
                composable(Screen.Profile.route) {
                    ProfileScreen(
                        user = currentUser,
                        posts = posts,
                        reels = reels,
                        selectedTab = profileTab,
                        onTabSelected = { viewModel.setProfileTab(it) },
                        onEditProfileClick = { viewModel.openEditProfile() },
                        onMutualsClick = { viewModel.openMutualsSheet() },
                        onShareProfileClick = {
                            viewModel.showSnack("Profile link copied: https://elvora.app/@${currentUser.username}")
                        },
                        onPostClick = { viewModel.openComments(it) }
                    )
                }

                // Discover / Explore
                composable(Screen.Discover.route) {
                    DiscoverScreen(
                        suggestedUsers = suggestedUsers,
                        posts = posts,
                        onFollowUser = { viewModel.toggleFollowUser(it) },
                        onPostClick = { viewModel.openComments(it) },
                        onUserClick = { viewModel.showSnack("Viewing profile of ${it.name}") }
                    )
                }

                // Notifications
                composable(Screen.Notifications.route) {
                    NotificationsScreen(
                        notifications = notifications,
                        onToggleFollowBack = { viewModel.toggleFollowNotificationUser(it) }
                    )
                }
            }
        }

        // Fullscreen 24-hour Moment Viewer Overlay
        activeMoment?.let { moment ->
            MomentViewerScreen(
                moment = moment,
                onClose = { viewModel.closeMoment() },
                onReplySent = { replyText ->
                    val targetConv = conversations.find { it.otherUser.id == moment.author.id }
                    if (targetConv != null) {
                        viewModel.sendChatMessage(targetConv.id, "Replied to story: $replyText")
                    }
                    viewModel.showSnack("Reply sent to ${moment.author.name} in Chat! 💬")
                    viewModel.closeMoment()
                },
                onReaction = { emoji ->
                    viewModel.showSnack("Reacted $emoji to ${moment.author.name}'s Moment!")
                }
            )
        }

        // Create Modal (Post, Reel, Moment)
        if (isCreateOpen) {
            CreateContentModal(
                onClose = { viewModel.closeCreate() },
                onCreatePost = { caption, tags, audio, location, res, url ->
                    viewModel.createPost(caption, tags, audio, location, res, url)
                },
                onCreateMoment = { caption, grad, res, url ->
                    viewModel.createMoment(caption, grad, res, url)
                },
                onCreateReel = { caption, audio, res ->
                    viewModel.createReel(caption, audio, res)
                }
            )
        }

        // Edit Profile Modal
        if (isEditProfileOpen) {
            EditProfileModal(
                currentUser = currentUser,
                onClose = { viewModel.closeEditProfile() },
                onSave = { name, username, bio, avatar ->
                    viewModel.updateProfile(name, username, bio, avatar)
                }
            )
        }

        // Mutual Connections Sheet
        if (isMutualsSheetOpen) {
            MutualsSheet(
                users = suggestedUsers,
                onFollowToggle = { viewModel.toggleFollowUser(it) },
                onClose = { viewModel.closeMutualsSheet() }
            )
        }

        // Bottom Sheets: Comments & Share
        activeCommentPost?.let { post ->
            CommentsBottomSheet(
                post = post,
                comments = commentsMap[post.id] ?: emptyList(),
                currentUserAvatar = currentUser.avatarUrl,
                onAddComment = { text -> viewModel.addComment(post.id, text) },
                onLikeComment = { cId -> viewModel.toggleLikeComment(post.id, cId) },
                onDismiss = { viewModel.closeComments() }
            )
        }

        activeSharePost?.let { post ->
            ShareBottomSheet(
                post = post,
                friends = suggestedUsers,
                onCopyLink = {
                    viewModel.copyPostLink(post.id)
                    viewModel.closeShare()
                },
                onRepost = {
                    viewModel.repostPost(post.id)
                    viewModel.closeShare()
                },
                onSendToFriend = { friend ->
                    val conv = conversations.find { it.otherUser.id == friend.id }
                    if (conv != null) {
                        viewModel.sendChatMessage(conv.id, "Shared post: https://elvora.app/post/${post.id}")
                    }
                    viewModel.showSnack("Sent post to ${friend.name} in Chat! 🚀")
                    viewModel.closeShare()
                },
                onAddToStory = {
                    viewModel.createMoment(
                        textCaption = "Check out @${post.author.username}'s post! ⚡",
                        bgGradient = listOf(0xFF0F0C20, 0xFF4C1D95),
                        mediaRes = post.mediaRes,
                        mediaUrl = post.mediaUrl
                    )
                    viewModel.closeShare()
                },
                onExternalShare = {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Check out this post on Elvora: https://elvora.app/post/${post.id}")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, "Share Elvora Post")
                    context.startActivity(shareIntent)
                    viewModel.closeShare()
                },
                onDismiss = { viewModel.closeShare() }
            )
        }

        // Floating Toast / Feedback Pill
        AnimatedVisibility(
            visible = snackMessage != null,
            enter = slideInVertically(initialOffsetY = { 100 }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { 100 }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 72.dp, start = 20.dp, end = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(ElvoraSurfaceDark)
                    .border(1.dp, ElvoraElectricViolet, RoundedCornerShape(24.dp))
                    .padding(horizontal = 18.dp, vertical = 10.dp)
            ) {
                Text(
                    text = snackMessage.orEmpty(),
                    color = ElvoraSilverPure,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
