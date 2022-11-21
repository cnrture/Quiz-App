package com.canerture.quizapp.domain.usecase

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.data.model.question.Question
import com.canerture.quizapp.domain.repository.QuestionRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GetQuestionsByCategoryUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    operator fun invoke(
        category: String,
        difficulty: String,
        type: String
    ): Flow<Resource<List<Question>>> = callbackFlow {
        questionRepository.getQuestionsByCategory(category, difficulty, type).collect {
            when (it) {
                Resource.Loading -> trySend(Resource.Loading)
                is Resource.Success -> {
                    if (it.data.results.isNullOrEmpty()) {
                        trySend(Resource.Error("Empty question list!"))
                    } else {
                        trySend(Resource.Success(it.data.results))
                    }
                }
                is Resource.Error -> {
                    trySend(Resource.Error(it.message))
                }
            }
        }

        awaitClose { channel.close() }
    }
}