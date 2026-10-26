package com.example.ui.navigation

sealed class Screen(val route: String, val title: String) {
    data object Home : Screen("home", "Home")
    data object Reels : Screen("reels", "Reels")
    data object Moments : Screen("moments", "Moments")
    data object Chat : Screen("chat", "Chat")
    data object Profile : Screen("profile", "Profile")
    data object Discover : Screen("discover", "Discover")
    data object Notifications : Screen("notifications", "Notifications")
    data object ChatDetail : Screen("chat_detail/{conversationId}", "Conversation") {
        fun createRoute(conversationId: String) = "chat_detail/$conversationId"
    }

    companion object {
        val bottomNavScreens = listOf(Home, Reels, Moments, Chat, Profile)
    }
}
