package com.example.myapplication.models

data class GameModel (
    val id: Int? = null,
    val cover: CoverModel? = null,
    val genres: List<GenreModel?> = emptyList(),
    val name: String? = null,
    val screenshotsUrl: List<ScreenshotModel?> = emptyList(),
    val similarGames: List<GameModel?> = emptyList(),
    val platforms: List<PlatformModel?> = emptyList(),
    val status: Int? = null,
    val summary: String? = null
    )