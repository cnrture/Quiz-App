package com.canerture.quizapp.data.source.remote

import com.canerture.quizapp.data.model.question.Result
import com.canerture.quizapp.data.model.token.Token
import kotlinx.coroutines.flow.Flow

interface QuestionDataSource {

    suspend fun getQuestionsByCategory(
        category: Int,
        type: String,
        token: String
    ): Result

    suspend fun getSessionToken(): Token
}