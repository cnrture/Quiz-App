package com.canerture.quizapp.domain.usecase

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.data.model.category.TriviaCategory
import com.canerture.quizapp.domain.repository.QuestionRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    operator fun invoke(): Flow<Resource<List<TriviaCategory>>> = callbackFlow {
        questionRepository.getCategories().collect {
            when (it) {
                Resource.Loading -> trySend(Resource.Loading)
                is Resource.Success -> {
                    if (it.data.triviaCategories.isNullOrEmpty()) {
                        trySend(Resource.Error("Empty category list!"))
                    } else {
                        trySend(Resource.Success(it.data.triviaCategories))
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