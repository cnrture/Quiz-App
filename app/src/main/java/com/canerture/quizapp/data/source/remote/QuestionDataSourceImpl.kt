package com.canerture.quizapp.data.source.remote

import com.canerture.quizapp.data.model.question.Result
import com.canerture.quizapp.data.model.token.Token
import com.canerture.quizapp.domain.source.remote.QuestionDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class QuestionDataSourceImpl @Inject constructor(
    private val questionService: QuestionService,
) : QuestionDataSource {

    override fun getQuestionsByCategory(
        category: Int,
        type: String,
        token: String,
    ): Flow<Result> = flow {
        emit(
            questionService.getQuestionsByCategory(
                category = category,
                type = type,
                token = token
            )
        )
    }

    override fun getSessionToken(): Flow<Token> = flow {
        emit(questionService.getSessionToken())
    }

    override fun resetSessionToken(token: String): Flow<Token> = flow {
        emit(questionService.resetSessionToken(token = token))
    }
}