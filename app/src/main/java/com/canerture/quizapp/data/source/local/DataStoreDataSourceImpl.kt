package com.canerture.quizapp.data.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.canerture.quizapp.BuildConfig
import com.canerture.quizapp.domain.source.local.DataStoreDataSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

class DataStoreDataSourceImpl(private val context: Context) : DataStoreDataSource {

    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = BuildConfig.DATASTORE_NAME)

    override fun saveToken(token: String): Flow<Boolean> = callbackFlow {
        try {
            context.datastore.edit {
                it[stringPreferencesKey("token")] = token
                trySend(true)
            }
        } catch (e: Exception) {
            trySend(false)
        }

        awaitClose { channel.close() }
    }

    override fun getToken(): Flow<String?> = context.datastore.data.map {
        it[stringPreferencesKey("token")]
    }
}