package com.canerture.quizapp.data.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.canerture.quizapp.common.Constants.DATASTORE_NAME
import com.canerture.quizapp.domain.source.local.DataStoreDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreDataSourceImpl(private val context: Context) : DataStoreDataSource {

    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

    override suspend fun saveToken(token: String) {
        context.datastore.edit {
            it[stringPreferencesKey("token")] = token
        }
    }

    override fun getToken(): Flow<String?> = context.datastore.data.map {
        it[stringPreferencesKey("token")]
    }
}