package com.canerture.quizapp.domain.source.local

import kotlinx.coroutines.flow.Flow

interface DataStoreDataSource {

    fun saveToken(token: String): Flow<Boolean>

    fun getToken(): Flow<String?>
}