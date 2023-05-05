package com.canerture.quizapp.data.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.canerture.quizapp.BuildConfig
import kotlinx.coroutines.flow.first

class DataStoreDataSourceImpl(private val context: Context) : DataStoreDataSource {

    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = BuildConfig.DATASTORE_NAME)

    override suspend fun saveToken(token: String) {
        context.datastore.edit {
            it[stringPreferencesKey("token")] = token
        }
    }

    override suspend fun getToken() = context.datastore.data.first()[stringPreferencesKey("token")]
}