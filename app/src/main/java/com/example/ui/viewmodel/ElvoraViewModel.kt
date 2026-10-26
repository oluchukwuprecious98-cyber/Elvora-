package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ChatConversation
import com.example.data.model.ChatMessage
import com.example.data.model.Comment
import com.example.data.model.Moment
import com.example.data.model.NotificationItem
import com.example.data.model.Post
import com.example.data.model.Reel
import com.example.data.model.User
import com.example.data.repository.ElvoraRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ElvoraScreen {
    HOME,
    DISCOVER,
    REELS,
    NOTIFICATIONS,
    PROFILE
}

enum class FeedTab {
    FOR_YOU,
    FOLLOWING,
    MUTUALS
}

enum class ProfileTab {
    POSTS,
    REELS,
    SAVED
}

class ElvoraViewModel(
    private val repository: ElvoraRepository = ElvoraRepository()
) : ViewModel() {

    val currentUser: StateFlow<User> = repository.currentUser
    val suggestedUsers: StateFlow<List<User>> = repository.suggestedUsers
    val moments: StateFlow<List<Moment>> = repository.moments
    val posts: StateFlow<List<Post>> = repository.posts
    val comments: StateFlow<Map<String, List<Comment>>> = repository.comments
    val reels: StateFlow<List<Reel>> = repository.reels
    val conversations: StateFlow<List<ChatConversation>> = repository.conversations
    val messages: StateFlow<Map<String, List<ChatMessage>>> = repository.messages
    val notifications: StateFlow<List<NotificationItem>> = repository.notifications

    // Screen State
    private val _currentScreen = MutableStateFlow(ElvoraScreen.HOME)
    val currentScreen: StateFlow<ElvoraScreen> = _currentScreen.asStateFlow()

    private val _feedTab = MutableStateFlow(FeedTab.FOR_YOU)
    val feedTab: StateFlow<FeedTab> = _feedTab.asStateFlow()

    private val _profileTab = MutableStateFlow(ProfileTab.POSTS)
    val profileTab: StateFlow<ProfileTab> = _profileTab.asStateFlow()

    // Modals and Active Overlays
    private val _activeMoment = MutableStateFlow<Moment?>(null)
    val activeMoment: StateFlow<Moment?> = _activeMoment.asStateFlow()

    private val _activeCommentPost = MutableStateFlow<Post?>(null)
    val activeCommentPost: StateFlow<Post?> = _activeCommentPost.asStateFlow()

    private val _activeSharePost = MutableStateFlow<Post?>(null)
    val activeSharePost: StateFlow<Post?> = _activeSharePost.asStateFlow()

    private val _activeConversation = MutableStateFlow<ChatConversation?>(null)
    val activeConversation: StateFlow<ChatConversation?> = _activeConversation.asStateFlow()

    private val _isChatListOpen = MutableStateFlow(false)
    val isChatListOpen: StateFlow<Boolean> = _isChatListOpen.asStateFlow()

    private val _isCreateOpen = MutableStateFlow(false)
    val isCreateOpen: StateFlow<Boolean> = _isCreateOpen.asStateFlow()

    private val _isEditProfileOpen = MutableStateFlow(false)
    val isEditProfileOpen: StateFlow<Boolean> = _isEditProfileOpen.asStateFlow()

    private val _isMutualsSheetOpen = MutableStateFlow(false)
    val isMutualsSheetOpen: StateFlow<Boolean> = _isMutualsSheetOpen.asStateFlow()

    // Search query in Discover
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Toast/Snackbar notifications
    private val _snackMessage = MutableStateFlow<String?>(null)
    val snackMessage: StateFlow<String?> = _snackMessage.asStateFlow()

    // Filtered Feed posts
    val filteredPosts: StateFlow<List<Post>> = combine(_postsState(), _feedTab) { posts, tab ->
        when (tab) {
            FeedTab.FOR_YOU -> posts
            FeedTab.FOLLOWING -> posts.filter { it.author.id == "user_maya" || it.author.id == "current_user" }
            FeedTab.MUTUALS -> posts.filter { it.author.id == "user_kai" || it.author.id == "user_aura" }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun _postsState(): StateFlow<List<Post>> = repository.posts

    fun setScreen(screen: ElvoraScreen) {
        _currentScreen.value = screen
        if (screen == ElvoraScreen.NOTIFICATIONS) {
            repository.markNotificationsAsRead()
        }
    }

    fun setFeedTab(tab: FeedTab) {
        _feedTab.value = tab
    }

    fun setProfileTab(tab: ProfileTab) {
        _profileTab.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openMoment(moment: Moment) {
        repository.markMomentViewed(moment.id)
        _activeMoment.value = moment
    }

    fun closeMoment() {
        _activeMoment.value = null
    }

    fun openComments(post: Post) {
        _activeCommentPost.value = post
    }

    fun closeComments() {
        _activeCommentPost.value = null
    }

    fun openShare(post: Post) {
        _activeSharePost.value = post
    }

    fun closeShare() {
        _activeSharePost.value = null
    }

    fun openChatList() {
        _isChatListOpen.value = true
    }

    fun closeChatList() {
        _isChatListOpen.value = false
        _activeConversation.value = null
    }

    fun openConversation(conv: ChatConversation) {
        _activeConversation.value = conv
    }

    fun closeConversation() {
        _activeConversation.value = null
    }

    fun openCreate() {
        _isCreateOpen.value = true
    }

    fun closeCreate() {
        _isCreateOpen.value = false
    }

    fun openEditProfile() {
        _isEditProfileOpen.value = true
    }

    fun closeEditProfile() {
        _isEditProfileOpen.value = false
    }

    fun openMutualsSheet() {
        _isMutualsSheetOpen.value = true
    }

    fun closeMutualsSheet() {
        _isMutualsSheetOpen.value = false
    }

    fun showSnack(msg: String) {
        _snackMessage.value = msg
        viewModelScope.launch {
            delay(2500)
            if (_snackMessage.value == msg) {
                _snackMessage.value = null
            }
        }
    }

    // Repository Delegation
    fun toggleLikePost(postId: String) = repository.toggleLikePost(postId)
    fun toggleSavePost(postId: String) {
        repository.toggleSavePost(postId)
        showSnack("Saved to your collection ✨")
    }
    fun repostPost(postId: String) {
        repository.repostPost(postId)
        showSnack("Reposted to your followers 🚀")
    }
    fun copyPostLink(postId: String) {
        val link = repository.sharePost(postId)
        showSnack("Link copied to clipboard: $link")
    }
    fun addComment(postId: String, text: String) {
        repository.addComment(postId, text)
        showSnack("Comment posted 💬")
    }
    fun toggleLikeComment(postId: String, commentId: String) = repository.toggleLikeComment(postId, commentId)

    fun toggleLikeReel(reelId: String) = repository.toggleLikeReel(reelId)
    fun toggleSaveReel(reelId: String) {
        repository.toggleSaveReel(reelId)
        showSnack("Reel saved to profile 🎬")
    }
    fun toggleFollowReelCreator(reelId: String) = repository.toggleFollowReelCreator(reelId)

    fun toggleFollowUser(userId: String) = repository.toggleFollowUser(userId)
    fun toggleFollowNotificationUser(notifId: String) = repository.toggleFollowNotificationUser(notifId)

    fun createPost(
        caption: String,
        hashtags: List<String>,
        audioTrack: String?,
        location: String?,
        mediaRes: Int?,
        mediaUrl: String?
    ) {
        repository.createPost(caption, hashtags, audioTrack, location, mediaRes, mediaUrl)
        closeCreate()
        setScreen(ElvoraScreen.HOME)
        showSnack("Your post is now live on Elvora! 📸✨")
    }

    fun createMoment(textCaption: String, bgGradient: List<Long>, mediaRes: Int?, mediaUrl: String?) {
        repository.createMoment(textCaption, bgGradient, mediaRes, mediaUrl)
        closeCreate()
        showSnack("Moment shared to your 24h story! ⏳")
    }

    fun createReel(caption: String, audioTrack: String, mediaRes: Int?) {
        repository.createReel(caption, audioTrack, mediaRes)
        closeCreate()
        setScreen(ElvoraScreen.REELS)
        showSnack("Reel published to Elvora 🎬⚡")
    }

    fun sendChatMessage(conversationId: String, text: String) {
        repository.sendChatMessage(conversationId, text)
    }

    fun sendVoiceNote(conversationId: String, durationSec: Int) {
        repository.sendVoiceNote(conversationId, durationSec)
        showSnack("Voice note sent 🎙️")
    }

    fun reactToMessage(conversationId: String, messageId: String, emoji: String) {
        repository.reactToMessage(conversationId, messageId, emoji)
    }

    fun updateProfile(name: String, username: String, bio: String, avatarUrl: String) {
        repository.updateProfile(name, username, bio, avatarUrl)
        closeEditProfile()
        showSnack("Profile updated successfully ✨")
    }
}
