package com.ruiz.emovie.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseRequestToken(
    val success: Boolean? = null,
    val expires_at: String? = null,
    val request_token: String? = null
)