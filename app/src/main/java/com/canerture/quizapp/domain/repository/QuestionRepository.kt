package com.canerture.quizapp.domain.repository

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.data.model.category.Category
import com.canerture.quizapp.data.model.question.Result
import com.canerture.quizapp.data.model.token.Token
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {

    fun getCategories(): Flow<Resource<Category>>

    fun getQuestionsByCategory(
        category: String,
        difficulty: String,
        type: String
    ): Flow<Resource<Result>>

    fun getSessionToken(): Flow<Resource<Token>>

    fun getTokenFromDataStore(): Flow<String?>

    suspend fun saveToken(token: String)
}