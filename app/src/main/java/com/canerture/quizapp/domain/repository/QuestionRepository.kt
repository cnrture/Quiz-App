package com.canerture.quizapp.domain.repository

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.data.model.question.Result
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {

    fun getQuestionsByCategory(
        category: Int,
        type: String
    ): Flow<Resource<Result>>
}