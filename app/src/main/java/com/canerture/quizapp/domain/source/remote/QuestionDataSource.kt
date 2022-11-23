package com.canerture.quizapp.domain.source.remote

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.data.model.question.Result
import com.canerture.quizapp.data.model.token.Token
import kotlinx.coroutines.flow.Flow

interface QuestionDataSource {

    fun getQuestionsByCategory(
        category: Int,
        type: String,
        token: String
    ): Flow<Resource<Result>>

    fun getSessionToken(): Flow<Resource<Token>>

    fun resetSessionToken(token: String): Flow<Resource<Token>>
}