package com.ruiz.emovie.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseGenres(
    val genres: List<MoviesGenres>? = null
)
