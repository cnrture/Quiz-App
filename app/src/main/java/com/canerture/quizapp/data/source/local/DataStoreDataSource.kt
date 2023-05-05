package com.canerture.quizapp.data.source.local

interface DataStoreDataSource {

    suspend fun saveToken(token: String)

    suspend fun getToken(): String?
}