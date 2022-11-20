package com.canerture.quizapp.domain.usecase

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.data.model.category.TriviaCategory
import com.canerture.quizapp.domain.repository.QuestionRepository
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class GetCategoriesUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    operator fun invoke(): Flow<Resource<List<TriviaCategory>>> = callbackFlow {
        questionRepository.getCategories().collect {
            when (it) {
                Resource.Loading -> send(Resource.Loading)
                is Resource.Success -> {
                    println(it.data)
                    if (it.data.triviaCategories.isNullOrEmpty()) {
                        send(Resource.Error("Empty category list!"))
                    } else {
                        send(Resource.Success(it.data.triviaCategories))
                    }
                }
                is Resource.Error -> {
                    send(Resource.Error(it.message))
                }
            }
        }

        awaitClose { channel.close() }
    }
}