package com.canerture.quizapp.domain.source.remote

import com.canerture.quizapp.data.model.question.Result
import com.canerture.quizapp.data.model.token.Token
import kotlinx.coroutines.flow.Flow

interface QuestionDataSource {

    fun getQuestionsByCategory(
        category: Int,
        type: String,
        token: String
    ): Flow<Result>

    fun getSessionToken(): Flow<Token>
}