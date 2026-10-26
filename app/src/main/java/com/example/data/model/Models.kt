package com.example.data.model

data class User(
    val id: String,
    val name: String,
    val username: String,
    val avatarUrl: String,
    val bio: String = "",
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val mutualFriendsCount: Int = 0,
    val isVerified: Boolean = false,
    val isFollowing: Boolean = false,
    val mutualWithText: String = "",
    val vibeTags: List<String> = emptyList()
)

data class Moment(
    val id: String,
    val author: User,
    val mediaRes: Int? = null,
    val mediaUrl: String? = null,
    val textCaption: String = "",
    val bgGradient: List<Long> = listOf(0xFF1F1235, 0xFF4A154B),
    val postedTimeAgo: String = "4h ago",
    val hoursRemaining: Int = 20,
    val isViewed: Boolean = false
)

data class Post(
    val id: String,
    val author: User,
    val caption: String,
    val hashtags: List<String> = emptyList(),
    val mediaRes: Int? = null,
    val mediaUrl: String? = null,
    val likesCount: Int,
    val isLiked: Boolean = false,
    val commentsCount: Int,
    val sharesCount: Int,
    val isSaved: Boolean = false,
    val isReposted: Boolean = false,
    val timeAgo: String,
    val audioTrack: String? = null,
    val location: String? = null
)

data class Comment(
    val id: String,
    val postId: String,
    val author: User,
    val text: String,
    val timeAgo: String,
    val likesCount: Int = 0,
    val isLiked: Boolean = false
)

data class Reel(
    val id: String,
    val creator: User,
    val caption: String,
    val audioTrack: String,
    val mediaRes: Int? = null,
    val mediaUrl: String? = null,
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val isFollowingCreator: Boolean = false,
    val viewsCount: String = "24.5K"
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val text: String? = null,
    val isVoiceNote: Boolean = false,
    val voiceDurationSec: Int? = null,
    val mediaRes: Int? = null,
    val timestamp: String,
    val isMine: Boolean,
    val reaction: String? = null
)

data class ChatConversation(
    val id: String,
    val otherUser: User,
    val lastMessage: String,
    val lastTime: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = true
)

enum class NotificationType {
    LIKE,
    COMMENT,
    FOLLOW,
    MENTION,
    REPOST
}

data class NotificationItem(
    val id: String,
    val type: NotificationType,
    val user: User,
    val content: String,
    val timeAgo: String,
    val isFollowingBack: Boolean = false,
    val isRead: Boolean = false
)
