package com.example.myapplication.models

data class FriendModel(
    val id: String? = null,
    val username: String? = null,
    val interests: List<String?> = emptyList(),
    val friends: List<FriendModel?> = emptyList(),
    val favoriteGames: List<GameModel?> = emptyList()
    )
