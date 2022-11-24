package com.ruiz.emovie.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiMarkFavoriteResponse(
    val success: Boolean = false,
    val status_code: Int = 0,
    val status_message: String = "",
)
