package com.canerture.quizapp.data.repository

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.data.model.question.Result
import com.canerture.quizapp.data.model.token.Token
import com.canerture.quizapp.domain.repository.QuestionRepository
import com.canerture.quizapp.domain.source.local.DataStoreDataSource
import com.canerture.quizapp.domain.source.remote.QuestionDataSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class QuestionsRepositoryImpl(
    private val questionDataSource: QuestionDataSource,
    private val dataStoreDataSource: DataStoreDataSource
) : QuestionRepository {

    override fun getQuestionsByCategory(
        category: Int,
        type: String,
        token: String
    ): Flow<Resource<Result>> = callbackFlow {
        try {
            questionDataSource.getQuestionsByCategory(category, type, token).collect {
                trySend(Resource.Success(it))
            }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message.orEmpty()))
        }

        awaitClose { channel.close() }
    }

    override fun getTokenFromDataStore(): Flow<String?> = callbackFlow {
        try {
            dataStoreDataSource.getToken().collect {
                trySend(it)
            }
        } catch (e: Exception) {
            trySend(e.message.orEmpty())
        }

        awaitClose { channel.close() }
    }

    override fun getSessionToken(): Flow<Resource<Token>> = callbackFlow {
        try {
            questionDataSource.getSessionToken().collect {
                trySend(Resource.Success(it))
            }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message.orEmpty()))
        }

        awaitClose { channel.close() }
    }

    override fun saveToken(token: String): Flow<Boolean> = callbackFlow {
        dataStoreDataSource.saveToken(token).collect {
            trySend(it)
        }

        awaitClose { channel.close() }
    }
}