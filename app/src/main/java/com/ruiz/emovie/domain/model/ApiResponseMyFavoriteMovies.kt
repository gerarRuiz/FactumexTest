package com.ruiz.emovie.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseMyFavoriteMovies(
    val page: Int = 1,
    val total_pages: Int? = null,
    val results: List<MyFavoriteMovies>? = null
)
