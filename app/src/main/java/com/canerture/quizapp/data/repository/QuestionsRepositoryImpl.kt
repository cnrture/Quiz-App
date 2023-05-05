package com.canerture.quizapp.data.repository

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.data.model.question.Result
import com.canerture.quizapp.data.model.token.Token
import com.canerture.quizapp.data.source.local.DataStoreDataSource
import com.canerture.quizapp.data.source.remote.QuestionDataSource
import com.canerture.quizapp.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class QuestionsRepositoryImpl(
    private val questionDataSource: QuestionDataSource,
    private val dataStoreDataSource: DataStoreDataSource
) : QuestionRepository {

    override fun getQuestionsByCategory(
        category: Int,
        type: String,
        token: String
    ) = flow<Resource<Result>> {
        emit(Resource.Success(questionDataSource.getQuestionsByCategory(category, type, token)))
    }.catch {
        emit(Resource.Error(it.message.orEmpty()))
    }

    override fun getTokenFromDataStore() = flow {
        emit(dataStoreDataSource.getToken())
    }

    override fun getSessionToken(): Flow<Resource<Token>> = flow<Resource<Token>> {
        emit(Resource.Success(questionDataSource.getSessionToken()))
    }.catch {
        emit(Resource.Error(it.message.orEmpty()))
    }

    override suspend fun saveToken(token: String) = dataStoreDataSource.saveToken(token)
}