package com.canerture.quizapp.domain.source.remote

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.data.model.category.Category
import com.canerture.quizapp.data.model.token.Token
import kotlinx.coroutines.flow.Flow

interface QuestionDataSource {

    fun getCategories(): Flow<Resource<Category>>

    fun getSessionToken(): Flow<Resource<Token>>
}