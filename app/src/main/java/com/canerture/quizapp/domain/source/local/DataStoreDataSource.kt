package com.canerture.quizapp.domain.source.local

import kotlinx.coroutines.flow.Flow

interface DataStoreDataSource {

    suspend fun saveToken(token: String)

    fun getToken(): Flow<String?>
}