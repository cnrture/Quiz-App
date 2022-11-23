package com.canerture.quizapp.data.repository

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.common.SessionManager
import com.canerture.quizapp.data.model.question.Result
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
        type: String
    ): Flow<Resource<Result>> = callbackFlow {

        dataStoreDataSource.getToken().collect {
            it?.let { token ->
                questionDataSource.getQuestionsByCategory(category, type, token)
                    .collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                when (result.data.responseCode) {
                                    3 -> getSessionToken()
                                    4 -> resetSessionToken(token)
                                    else -> trySend(Resource.Success(result.data))
                                }
                            }
                            is Resource.Error -> {
                                trySend(Resource.Error(result.message))
                            }
                        }
                    }
            } ?: kotlin.run {
                getSessionToken()
            }
        }

        awaitClose { channel.close() }
    }

    private fun getSessionToken() = callbackFlow {
        questionDataSource.getSessionToken().collect {
            when (it) {
                is Resource.Success -> {
                    it.data.token?.let { token ->
                        dataStoreDataSource.saveToken(token)
                        SessionManager.sessionToken = token
                    } ?: kotlin.run {
                        trySend(Resource.Error("Something went wrong!"))
                    }
                }
                is Resource.Error -> {
                    trySend(Resource.Error(it.message))
                }
            }
        }
    }

    private fun resetSessionToken(token: String) = callbackFlow {
        questionDataSource.resetSessionToken(token).collect {
            when (it) {
                is Resource.Success -> {
                    it.data.token?.let { token ->
                        dataStoreDataSource.saveToken(token)
                        SessionManager.sessionToken = token
                    } ?: kotlin.run {
                        trySend(Resource.Error("Something went wrong!"))
                    }
                }
                is Resource.Error -> {
                    trySend(Resource.Error(it.message))
                }
            }
        }
    }

    fun getTokenFromDataStore(): Flow<String?> = dataStoreDataSource.getToken()

    suspend fun saveToken(token: String) {
        dataStoreDataSource.saveToken(token)
    }
}