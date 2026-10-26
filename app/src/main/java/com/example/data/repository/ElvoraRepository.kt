package com.example.data.repository

import com.example.R
import com.example.data.model.ChatConversation
import com.example.data.model.ChatMessage
import com.example.data.model.Comment
import com.example.data.model.Moment
import com.example.data.model.NotificationItem
import com.example.data.model.NotificationType
import com.example.data.model.Post
import com.example.data.model.Reel
import com.example.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class ElvoraRepository {

    // Current Logged-in User
    private val _currentUser = MutableStateFlow(
        User(
            id = "current_user",
            name = "Elena Vance",
            username = "elena.vance",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
            bio = "Digital architect & sonic visualizer ⚡ Creating ethereal worlds on Elvora. Exploring nocturnal Tokyo & neon acoustics.",
            followersCount = 4820,
            followingCount = 340,
            mutualFriendsCount = 42,
            isVerified = true,
            vibeTags = listOf("Cyberpunk", "AudioVis", "Midnight", "Tokyo2049", "Synthwave")
        )
    )
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    // Suggested Friends / Mutual Connections
    private val _suggestedUsers = MutableStateFlow(
        listOf(
            User(
                id = "user_kai",
                name = "Kai Nova",
                username = "kai.nova",
                avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80",
                bio = "Sound design & modular synthesis 🎹",
                followersCount = 12400,
                followingCount = 412,
                mutualFriendsCount = 9,
                mutualWithText = "Followed by @maya.design + 8 others",
                isVerified = true,
                isFollowing = false
            ),
            User(
                id = "user_maya",
                name = "Maya Lin",
                username = "maya.design",
                avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80",
                bio = "Futuristic UI & 3D generative forms ✨",
                followersCount = 28900,
                followingCount = 590,
                mutualFriendsCount = 14,
                mutualWithText = "Followed by @elena.vance + 13 others",
                isVerified = true,
                isFollowing = true
            ),
            User(
                id = "user_zane",
                name = "Zane Cross",
                username = "zane.cyber",
                avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80",
                bio = "Nocturnal urban street & neon film 📸",
                followersCount = 8300,
                followingCount = 310,
                mutualFriendsCount = 5,
                mutualWithText = "Mutual friend with @kai.nova",
                isVerified = false,
                isFollowing = false
            ),
            User(
                id = "user_aura",
                name = "Aura Bloom",
                username = "aura.glitch",
                avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=300&q=80",
                bio = "Fashion meets virtual reality garments 🔮",
                followersCount = 41200,
                followingCount = 780,
                mutualFriendsCount = 18,
                mutualWithText = "Followed by @maya.design + 17 others",
                isVerified = true,
                isFollowing = false
            )
        )
    )
    val suggestedUsers: StateFlow<List<User>> = _suggestedUsers.asStateFlow()

    // 24-hour Elvora Moments (Stories)
    private val _moments = MutableStateFlow(
        listOf(
            Moment(
                id = "moment_my",
                author = _currentUser.value,
                mediaRes = R.drawable.elvora_post_art_1793042399481,
                textCaption = "Midnight render finished ✨ #elvora",
                postedTimeAgo = "2h ago",
                hoursRemaining = 22,
                isViewed = false
            ),
            Moment(
                id = "moment_1",
                author = User(
                    id = "user_maya",
                    name = "Maya Lin",
                    username = "maya.design",
                    avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80",
                    isVerified = true
                ),
                mediaRes = R.drawable.elvora_reel_cover_1793042413260,
                textCaption = "Backstage at Tokyo Cyber Club 2049 💜",
                postedTimeAgo = "4h ago",
                hoursRemaining = 20,
                isViewed = false
            ),
            Moment(
                id = "moment_2",
                author = User(
                    id = "user_kai",
                    name = "Kai Nova",
                    username = "kai.nova",
                    avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80",
                    isVerified = true
                ),
                textCaption = "Dropping new synth stems today at midnight! Who wants early access? 🎹⚡",
                bgGradient = listOf(0xFF0F0C20, 0xFF4C1D95),
                postedTimeAgo = "6h ago",
                hoursRemaining = 18,
                isViewed = false
            ),
            Moment(
                id = "moment_3",
                author = User(
                    id = "user_aura",
                    name = "Aura Bloom",
                    username = "aura.glitch",
                    avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=300&q=80",
                    isVerified = true
                ),
                textCaption = "Silver chrome fabric prototype ready for the runway! 🪞✨",
                bgGradient = listOf(0xFF1E1B4B, 0xFF7C3AED),
                postedTimeAgo = "9h ago",
                hoursRemaining = 15,
                isViewed = false
            )
        )
    )
    val moments: StateFlow<List<Moment>> = _moments.asStateFlow()

    // Posts Feed
    private val _posts = MutableStateFlow(
        listOf(
            Post(
                id = "post_1",
                author = User(
                    id = "user_maya",
                    name = "Maya Lin",
                    username = "maya.design",
                    avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80",
                    isVerified = true
                ),
                caption = "Metropolis Neon 2049. The skyline of Neo-Tokyo reflecting across the water under midnight violet skies. Rendered live in real-time engine.",
                hashtags = listOf("elvora", "cyberpunk", "tokyo", "3dart", "neonaesthetic"),
                mediaRes = R.drawable.elvora_post_art_1793042399481,
                likesCount = 1420,
                isLiked = false,
                commentsCount = 89,
                sharesCount = 156,
                isSaved = false,
                isReposted = false,
                timeAgo = "1h ago",
                audioTrack = "Violet Echoes • Original Mix",
                location = "Neo Tokyo Harbor, District 7"
            ),
            Post(
                id = "post_2",
                author = User(
                    id = "user_aura",
                    name = "Aura Bloom",
                    username = "aura.glitch",
                    avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=300&q=80",
                    isVerified = true
                ),
                caption = "Silver reflections and midnight electric violet stitching. Exploring wearable cyber-couture in the dark room. What vibe do you feel? ⚡",
                hashtags = listOf("elvora", "fashion", "futuristic", "cyberclub"),
                mediaRes = R.drawable.elvora_reel_cover_1793042413260,
                likesCount = 3890,
                isLiked = true,
                commentsCount = 214,
                sharesCount = 430,
                isSaved = true,
                isReposted = false,
                timeAgo = "3h ago",
                audioTrack = "Synthetic Dreams • Aura Bloom",
                location = "Cyber Club Neon Den"
            ),
            Post(
                id = "post_3",
                author = User(
                    id = "user_kai",
                    name = "Kai Nova",
                    username = "kai.nova",
                    avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80",
                    isVerified = true
                ),
                caption = "Late night modular patches running through analog tape saturation. Connecting with fellow creators on Elvora has been such a breath of fresh air! Drop your favorite synth in comments 🎹",
                hashtags = listOf("musicproducer", "synthesizer", "elvora", "creators"),
                mediaRes = null,
                mediaUrl = "https://images.unsplash.com/photo-1598488035139-bdbb2231ce04?auto=format&fit=crop&w=800&q=80",
                likesCount = 874,
                isLiked = false,
                commentsCount = 53,
                sharesCount = 38,
                isSaved = false,
                isReposted = false,
                timeAgo = "5h ago",
                audioTrack = "Analog Horizon • Kai Nova",
                location = "Studio Nova"
            )
        )
    )
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    // Comments map: postId -> List<Comment>
    private val _comments = MutableStateFlow<Map<String, List<Comment>>>(
        mapOf(
            "post_1" to listOf(
                Comment(
                    id = "c_1",
                    postId = "post_1",
                    author = User(
                        id = "user_kai",
                        name = "Kai Nova",
                        username = "kai.nova",
                        avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80",
                        isVerified = true
                    ),
                    text = "The reflection rendering on the water is legendary! 🔥",
                    timeAgo = "45m ago",
                    likesCount = 24,
                    isLiked = true
                ),
                Comment(
                    id = "c_2",
                    postId = "post_1",
                    author = User(
                        id = "user_aura",
                        name = "Aura Bloom",
                        username = "aura.glitch",
                        avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=300&q=80",
                        isVerified = true
                    ),
                    text = "The violet color grading is so distinctive, love this aesthetic! 💜",
                    timeAgo = "30m ago",
                    likesCount = 12,
                    isLiked = false
                )
            ),
            "post_2" to listOf(
                Comment(
                    id = "c_3",
                    postId = "post_2",
                    author = _currentUser.value,
                    text = "That silver jacket is insane! Need one for my live set ⚡",
                    timeAgo = "2h ago",
                    likesCount = 38,
                    isLiked = true
                )
            )
        )
    )
    val comments: StateFlow<Map<String, List<Comment>>> = _comments.asStateFlow()

    // Elvora Reels
    private val _reels = MutableStateFlow(
        listOf(
            Reel(
                id = "reel_1",
                creator = User(
                    id = "user_aura",
                    name = "Aura Bloom",
                    username = "aura.glitch",
                    avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=300&q=80",
                    isVerified = true
                ),
                caption = "Cyber club night 2049 ✨ Silver threads glow under blacklight #elvora #reels #futurism #neon",
                audioTrack = "Midnight Odyssey • Aura Bloom & Kai",
                mediaRes = R.drawable.elvora_reel_cover_1793042413260,
                likesCount = 18400,
                commentsCount = 620,
                sharesCount = 1290,
                isLiked = true,
                isSaved = true,
                isFollowingCreator = false,
                viewsCount = "89.2K"
            ),
            Reel(
                id = "reel_2",
                creator = User(
                    id = "user_maya",
                    name = "Maya Lin",
                    username = "maya.design",
                    avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80",
                    isVerified = true
                ),
                caption = "Animating the futuristic transit rails of Neo Tokyo in 60fps 🚄 Watch till the end! #motiondesign #cyberpunk",
                audioTrack = "Neon Velocity • Cyber Pulse",
                mediaRes = R.drawable.elvora_post_art_1793042399481,
                likesCount = 26500,
                commentsCount = 890,
                sharesCount = 2410,
                isLiked = false,
                isSaved = false,
                isFollowingCreator = true,
                viewsCount = "142K"
            ),
            Reel(
                id = "reel_3",
                creator = User(
                    id = "user_zane",
                    name = "Zane Cross",
                    username = "zane.cyber",
                    avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80",
                    isVerified = false
                ),
                caption = "Midnight rain in Shibuya 🌧️ 35mm lens with custom electric violet LUT filter #nightwalk #tokyo #elvora",
                audioTrack = "Raindrops on Glass • Lo-Fi Midnight",
                mediaRes = null,
                mediaUrl = "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=800&q=80",
                likesCount = 9200,
                commentsCount = 180,
                sharesCount = 540,
                isLiked = false,
                isSaved = false,
                isFollowingCreator = false,
                viewsCount = "45.7K"
            )
        )
    )
    val reels: StateFlow<List<Reel>> = _reels.asStateFlow()

    // Chat Conversations
    private val _conversations = MutableStateFlow(
        listOf(
            ChatConversation(
                id = "conv_maya",
                otherUser = User(
                    id = "user_maya",
                    name = "Maya Lin",
                    username = "maya.design",
                    avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80",
                    isVerified = true
                ),
                lastMessage = "🎙️ Voice note (0:14) • Loved the new render!",
                lastTime = "12:40 PM",
                unreadCount = 2,
                isOnline = true
            ),
            ChatConversation(
                id = "conv_kai",
                otherUser = User(
                    id = "user_kai",
                    name = "Kai Nova",
                    username = "kai.nova",
                    avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80",
                    isVerified = true
                ),
                lastMessage = "Sent you the synth stem package! 🎹",
                lastTime = "Yesterday",
                unreadCount = 0,
                isOnline = false
            ),
            ChatConversation(
                id = "conv_aura",
                otherUser = User(
                    id = "user_aura",
                    name = "Aura Bloom",
                    username = "aura.glitch",
                    avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=300&q=80",
                    isVerified = true
                ),
                lastMessage = "Let's collaborate on the next reel series!",
                lastTime = "2d ago",
                unreadCount = 0,
                isOnline = true
            )
        )
    )
    val conversations: StateFlow<List<ChatConversation>> = _conversations.asStateFlow()

    // Chat Messages map: conversationId -> List<ChatMessage>
    private val _messages = MutableStateFlow<Map<String, List<ChatMessage>>>(
        mapOf(
            "conv_maya" to listOf(
                ChatMessage(
                    id = "m1",
                    senderId = "user_maya",
                    text = "Hey Elena! Saw your new Elvora moment from the studio!",
                    timestamp = "12:35 PM",
                    isMine = false
                ),
                ChatMessage(
                    id = "m2",
                    senderId = "current_user",
                    text = "Thank you Maya! The electric violet lighting turned out even better than planned.",
                    timestamp = "12:37 PM",
                    isMine = true
                ),
                ChatMessage(
                    id = "m3",
                    senderId = "user_maya",
                    isVoiceNote = true,
                    voiceDurationSec = 14,
                    timestamp = "12:40 PM",
                    isMine = false,
                    reaction = "❤️"
                )
            ),
            "conv_kai" to listOf(
                ChatMessage(
                    id = "m4",
                    senderId = "current_user",
                    text = "Hey Kai, do you have that bass synth loop from yesterday's live session?",
                    timestamp = "4:15 PM",
                    isMine = true
                ),
                ChatMessage(
                    id = "m5",
                    senderId = "user_kai",
                    text = "Sent you the synth stem package! 🎹 Check your drive link.",
                    timestamp = "4:20 PM",
                    isMine = false,
                    reaction = "🔥"
                )
            )
        )
    )
    val messages: StateFlow<Map<String, List<ChatMessage>>> = _messages.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow(
        listOf(
            NotificationItem(
                id = "n_1",
                type = NotificationType.LIKE,
                user = User(
                    id = "user_aura",
                    name = "Aura Bloom",
                    username = "aura.glitch",
                    avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=300&q=80",
                    isVerified = true
                ),
                content = "liked your post 'Metropolis Neon 2049'",
                timeAgo = "12m ago",
                isRead = false
            ),
            NotificationItem(
                id = "n_2",
                type = NotificationType.FOLLOW,
                user = User(
                    id = "user_zane",
                    name = "Zane Cross",
                    username = "zane.cyber",
                    avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80"
                ),
                content = "started following you. Mutual with @kai.nova",
                timeAgo = "45m ago",
                isFollowingBack = false,
                isRead = false
            ),
            NotificationItem(
                id = "n_3",
                type = NotificationType.COMMENT,
                user = User(
                    id = "user_kai",
                    name = "Kai Nova",
                    username = "kai.nova",
                    avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80",
                    isVerified = true
                ),
                content = "commented: 'The reflection rendering on the water is legendary! 🔥'",
                timeAgo = "1h ago",
                isRead = true
            ),
            NotificationItem(
                id = "n_4",
                type = NotificationType.MENTION,
                user = User(
                    id = "user_maya",
                    name = "Maya Lin",
                    username = "maya.design",
                    avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80",
                    isVerified = true
                ),
                content = "mentioned you in a reel: 'Collab with @elena.vance soon!'",
                timeAgo = "3h ago",
                isRead = true
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // ACTIONS

    fun toggleLikePost(postId: String) {
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    val newLiked = !post.isLiked
                    val newCount = if (newLiked) post.likesCount + 1 else (post.likesCount - 1).coerceAtLeast(0)
                    post.copy(isLiked = newLiked, likesCount = newCount)
                } else post
            }
        }
    }

    fun toggleSavePost(postId: String) {
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) post.copy(isSaved = !post.isSaved) else post
            }
        }
    }

    fun repostPost(postId: String) {
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    val newRepost = !post.isReposted
                    val newShares = if (newRepost) post.sharesCount + 1 else post.sharesCount
                    post.copy(isReposted = newRepost, sharesCount = newShares)
                } else post
            }
        }
    }

    fun sharePost(postId: String): String {
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) post.copy(sharesCount = post.sharesCount + 1) else post
            }
        }
        return "https://elvora.app/post/$postId"
    }

    fun addComment(postId: String, text: String) {
        if (text.isBlank()) return
        val newComment = Comment(
            id = "comment_${UUID.randomUUID().toString().take(8)}",
            postId = postId,
            author = _currentUser.value,
            text = text.trim(),
            timeAgo = "Just now",
            likesCount = 0,
            isLiked = false
        )
        _comments.update { currentMap ->
            val existing = currentMap[postId] ?: emptyList()
            currentMap + (postId to (listOf(newComment) + existing))
        }
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) post.copy(commentsCount = post.commentsCount + 1) else post
            }
        }
    }

    fun toggleLikeComment(postId: String, commentId: String) {
        _comments.update { currentMap ->
            val list = currentMap[postId] ?: return@update currentMap
            val updated = list.map { comment ->
                if (comment.id == commentId) {
                    val newLiked = !comment.isLiked
                    val count = if (newLiked) comment.likesCount + 1 else (comment.likesCount - 1).coerceAtLeast(0)
                    comment.copy(isLiked = newLiked, likesCount = count)
                } else comment
            }
            currentMap + (postId to updated)
        }
    }

    fun toggleLikeReel(reelId: String) {
        _reels.update { list ->
            list.map { reel ->
                if (reel.id == reelId) {
                    val newLiked = !reel.isLiked
                    val newCount = if (newLiked) reel.likesCount + 1 else (reel.likesCount - 1).coerceAtLeast(0)
                    reel.copy(isLiked = newLiked, likesCount = newCount)
                } else reel
            }
        }
    }

    fun toggleSaveReel(reelId: String) {
        _reels.update { list ->
            list.map { reel ->
                if (reel.id == reelId) reel.copy(isSaved = !reel.isSaved) else reel
            }
        }
    }

    fun toggleFollowReelCreator(reelId: String) {
        _reels.update { list ->
            list.map { reel ->
                if (reel.id == reelId) reel.copy(isFollowingCreator = !reel.isFollowingCreator) else reel
            }
        }
    }

    fun toggleFollowUser(userId: String) {
        _suggestedUsers.update { list ->
            list.map { user ->
                if (user.id == userId) {
                    val newFollowing = !user.isFollowing
                    user.copy(
                        isFollowing = newFollowing,
                        followersCount = if (newFollowing) user.followersCount + 1 else user.followersCount - 1
                    )
                } else user
            }
        }
    }

    fun toggleFollowNotificationUser(notifId: String) {
        _notifications.update { list ->
            list.map { notif ->
                if (notif.id == notifId) notif.copy(isFollowingBack = !notif.isFollowingBack) else notif
            }
        }
    }

    fun markNotificationsAsRead() {
        _notifications.update { list ->
            list.map { it.copy(isRead = true) }
        }
    }

    fun markMomentViewed(momentId: String) {
        _moments.update { list ->
            list.map { moment ->
                if (moment.id == momentId) moment.copy(isViewed = true) else moment
            }
        }
    }

    fun createPost(
        caption: String,
        hashtags: List<String>,
        audioTrack: String?,
        location: String?,
        mediaRes: Int?,
        mediaUrl: String?
    ) {
        val newPost = Post(
            id = "post_${UUID.randomUUID().toString().take(8)}",
            author = _currentUser.value,
            caption = caption,
            hashtags = hashtags,
            mediaRes = mediaRes ?: R.drawable.elvora_post_art_1793042399481,
            mediaUrl = mediaUrl,
            likesCount = 1,
            isLiked = true,
            commentsCount = 0,
            sharesCount = 0,
            isSaved = false,
            timeAgo = "Just now",
            audioTrack = audioTrack ?: "Elvora Sonic Pulse • Original",
            location = location ?: "Elvora Studio"
        )
        _posts.update { listOf(newPost) + it }
        _currentUser.update { it.copy(followersCount = it.followersCount + 1) }
    }

    fun createMoment(textCaption: String, bgGradient: List<Long>, mediaRes: Int?, mediaUrl: String?) {
        val newMoment = Moment(
            id = "moment_${UUID.randomUUID().toString().take(8)}",
            author = _currentUser.value,
            mediaRes = mediaRes,
            mediaUrl = mediaUrl,
            textCaption = textCaption,
            bgGradient = bgGradient,
            postedTimeAgo = "Just now",
            hoursRemaining = 24,
            isViewed = false
        )
        _moments.update { listOf(newMoment) + it }
    }

    fun createReel(caption: String, audioTrack: String, mediaRes: Int?) {
        val newReel = Reel(
            id = "reel_${UUID.randomUUID().toString().take(8)}",
            creator = _currentUser.value,
            caption = caption,
            audioTrack = audioTrack,
            mediaRes = mediaRes ?: R.drawable.elvora_reel_cover_1793042413260,
            likesCount = 0,
            commentsCount = 0,
            sharesCount = 0,
            isLiked = false,
            isSaved = false,
            isFollowingCreator = false,
            viewsCount = "1"
        )
        _reels.update { listOf(newReel) + it }
    }

    fun sendChatMessage(conversationId: String, text: String) {
        if (text.isBlank()) return
        val newMsg = ChatMessage(
            id = "msg_${UUID.randomUUID().toString().take(8)}",
            senderId = "current_user",
            text = text.trim(),
            timestamp = "Just now",
            isMine = true
        )
        _messages.update { currentMap ->
            val list = currentMap[conversationId] ?: emptyList()
            currentMap + (conversationId to (list + newMsg))
        }
        _conversations.update { list ->
            list.map { conv ->
                if (conv.id == conversationId) conv.copy(lastMessage = text.trim(), lastTime = "Just now") else conv
            }
        }
    }

    fun sendVoiceNote(conversationId: String, durationSec: Int = 10) {
        val newMsg = ChatMessage(
            id = "msg_${UUID.randomUUID().toString().take(8)}",
            senderId = "current_user",
            isVoiceNote = true,
            voiceDurationSec = durationSec,
            timestamp = "Just now",
            isMine = true
        )
        _messages.update { currentMap ->
            val list = currentMap[conversationId] ?: emptyList()
            currentMap + (conversationId to (list + newMsg))
        }
        _conversations.update { list ->
            list.map { conv ->
                if (conv.id == conversationId) conv.copy(lastMessage = "🎙️ Voice note (0:${if (durationSec < 10) "0$durationSec" else "$durationSec"})", lastTime = "Just now") else conv
            }
        }
    }

    fun reactToMessage(conversationId: String, messageId: String, emoji: String) {
        _messages.update { currentMap ->
            val list = currentMap[conversationId] ?: return@update currentMap
            val updated = list.map { msg ->
                if (msg.id == messageId) {
                    val newReaction = if (msg.reaction == emoji) null else emoji
                    msg.copy(reaction = newReaction)
                } else msg
            }
            currentMap + (conversationId to updated)
        }
    }

    fun updateProfile(name: String, username: String, bio: String, avatarUrl: String) {
        _currentUser.update {
            it.copy(
                name = name.trim().ifEmpty { it.name },
                username = username.trim().removePrefix("@").ifEmpty { it.username },
                bio = bio.trim(),
                avatarUrl = avatarUrl.trim().ifEmpty { it.avatarUrl }
            )
        }
    }
}
