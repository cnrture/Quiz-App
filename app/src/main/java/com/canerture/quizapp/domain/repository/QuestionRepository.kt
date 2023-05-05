package com.canerture.quizapp.domain.repository

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.data.model.question.Result
import com.canerture.quizapp.data.model.token.Token
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {

    fun getQuestionsByCategory(
        category: Int,
        type: String,
        token: String
    ): Flow<Resource<Result>>

    fun getTokenFromDataStore(): Flow<String?>

    fun getSessionToken(): Flow<Resource<Token>>

    suspend fun saveToken(token: String)
}