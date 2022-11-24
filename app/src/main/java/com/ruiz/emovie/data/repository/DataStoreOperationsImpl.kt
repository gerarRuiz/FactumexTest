package com.ruiz.emovie.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ruiz.emovie.domain.repository.DataStoreOperations
import com.ruiz.emovie.util.constants.ConstantsPreferences.PREFERENCES_NAME
import com.ruiz.emovie.util.constants.ConstantsPreferences.PREFERENCE_ACCOUNT_ID
import com.ruiz.emovie.util.constants.ConstantsPreferences.PREFERENCE_REQUEST_TOKEN
import com.ruiz.emovie.util.constants.ConstantsPreferences.PREFERENCE_REQUEST_TOKEN_EXPIRE_AT
import com.ruiz.emovie.util.constants.ConstantsPreferences.PREFERENCE_SESSION_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class DataStoreOperationsImpl(context: Context) : DataStoreOperations {

    private val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)
    private val dataStore = context.dataStore

    private object PreferencesKey {
        val requestToken = stringPreferencesKey(name = PREFERENCE_REQUEST_TOKEN)
        val requestTokenExpireAt = stringPreferencesKey(name = PREFERENCE_REQUEST_TOKEN_EXPIRE_AT)
        val sesssionId = stringPreferencesKey(name = PREFERENCE_SESSION_ID)
        val accountId = stringPreferencesKey(name = PREFERENCE_ACCOUNT_ID)
    }

    override suspend fun saveRequestToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.requestToken] = token
        }
    }

    override fun readRequestToken(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val requestTokenState = preferences[PreferencesKey.requestToken] ?: ""
                requestTokenState
            }
    }

    override suspend fun saveSessionId(sessionId: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.sesssionId] = sessionId
        }
    }

    override fun readSessionId(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val sessionIdState = preferences[PreferencesKey.sesssionId] ?: ""
                sessionIdState
            }
    }

    override suspend fun saveRequestTokenExpireAt(expireAt: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.requestTokenExpireAt] = expireAt
        }
    }

    override fun readRequestTokenExpireAt(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val expireAtState = preferences[PreferencesKey.requestTokenExpireAt] ?: ""
                expireAtState
            }
    }

    override suspend fun saveAccountId(accountId: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.accountId] = accountId
        }
    }

    override fun readAccountId(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val accountId = preferences[PreferencesKey.accountId] ?: ""
                accountId
            }
    }
}