package com.ruiz.emovie.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiMarkFavoriteRequest(
    val media_type: String = "",
    val media_id: Int = 0,
    val favorite: Boolean? = null
)
