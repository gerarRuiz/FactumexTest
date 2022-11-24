package com.ruiz.emovie.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseSessionId(
    val success: Boolean? = null,
    val session_id: String? = null
)
