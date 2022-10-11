package com.example.myapplication.models

data class NotificationModel(
    var type: String? = null,       //"friendRequest" or "gameRecommendation"
    var gameId: String? = null,
    var gameName: String? = null,
    var userId: String? = null,
    var userUsername: String? = null,
    var cover: String? = null,
    var genres: String? = null,
    var platforms: String? = null,
    var status: Int? = null,
    var summary: String? = null
)
