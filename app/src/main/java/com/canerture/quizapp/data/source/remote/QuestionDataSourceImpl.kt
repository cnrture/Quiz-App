package com.canerture.quizapp.data.source.remote

import com.canerture.quizapp.data.model.question.Result
import com.canerture.quizapp.data.model.token.Token
import com.canerture.quizapp.domain.source.remote.QuestionDataSource
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class QuestionDataSourceImpl @Inject constructor(
    private val questionService: QuestionService,
) : QuestionDataSource {

    override fun getQuestionsByCategory(
        category: Int,
        type: String,
        token: String,
    ): Flow<Result> = callbackFlow {
        trySend(questionService.getQuestionsByCategory(category, type, token))

        awaitClose { channel.close() }
    }

    override fun getSessionToken(): Flow<Token> = callbackFlow {
        trySend(questionService.getSessionToken())

        awaitClose { channel.close() }
    }
}