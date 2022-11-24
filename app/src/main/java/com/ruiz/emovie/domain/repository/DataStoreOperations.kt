package com.ruiz.emovie.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreOperations {

    suspend fun saveRequestToken(token: String)

    fun readRequestToken(): Flow<String>

    suspend fun saveRequestTokenExpireAt(expireAt: String)

    fun readRequestTokenExpireAt(): Flow<String>

    suspend fun saveSessionId(sessionId: String)

    fun readSessionId(): Flow<String>

    suspend fun saveAccountId(accountId: String)

    fun readAccountId(): Flow<String>

}