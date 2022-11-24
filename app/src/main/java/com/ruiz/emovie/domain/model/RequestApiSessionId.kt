package com.ruiz.emovie.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RequestApiSessionId(
    val request_token: String = ""
)
