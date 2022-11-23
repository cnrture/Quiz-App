package com.canerture.quizapp.domain.usecase

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.domain.model.question.QuestionUI
import com.canerture.quizapp.domain.repository.QuestionRepository
import com.canerture.quizapp.mapper.toQuestionUIList
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GetQuestionsByCategoryUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    operator fun invoke(
        category: Int,
        type: String
    ): Flow<Resource<List<QuestionUI>>> = callbackFlow {
        questionRepository.getQuestionsByCategory(category, type)
            .collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data.questions?.let {
                            trySend(Resource.Success(it.toQuestionUIList()))
                        } ?: kotlin.run {
                            trySend(Resource.Error("Empty question list!"))
                        }
                    }
                    is Resource.Error -> {
                        trySend(Resource.Error(result.message))
                    }
                }
            }

        awaitClose { channel.close() }
    }
}