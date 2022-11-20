package com.canerture.quizapp.data.repository

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.data.model.category.Category
import com.canerture.quizapp.data.model.token.Token
import com.canerture.quizapp.domain.repository.DataStoreDataSource
import com.canerture.quizapp.domain.repository.QuestionRepository
import com.canerture.quizapp.domain.source.remote.QuestionDataSource
import kotlinx.coroutines.flow.Flow

class QuestionsRepositoryImpl(
    private val questionDataSource: QuestionDataSource,
    private val dataStoreDataSource: DataStoreDataSource
) : QuestionRepository {

    override fun getCategories(): Flow<Resource<Category>> = questionDataSource.getCategories()

    override fun getSessionToken(): Flow<Resource<Token>> =
        questionDataSource.getSessionToken()

    override fun getTokenFromDataStore(): Flow<String?> = dataStoreDataSource.getToken()

    override suspend fun saveToken(token: String) {
        dataStoreDataSource.saveToken(token)
    }
}