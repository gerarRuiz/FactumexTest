package com.ruiz.emovie.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseAccount(
    val id: Long,
    val name: String? = null,
    val username: String? = null,
    val avatar: AvatarData? = null
)

@Serializable
data class AvatarData(
    val tmdb: AvatarPath? = null
)

@Serializable
data class AvatarPath(
    val avatar_path: String? = null
)
