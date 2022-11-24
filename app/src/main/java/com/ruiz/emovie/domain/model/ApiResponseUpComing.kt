package com.ruiz.emovie.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseUpComing(
    val page: Int = 1,
    val totalPages: Int? = null,
    val results: List<MovieUpcoming>? = null
)