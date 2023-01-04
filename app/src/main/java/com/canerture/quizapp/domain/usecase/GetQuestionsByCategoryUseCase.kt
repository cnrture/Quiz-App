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
    private val questionRepository: QuestionRepository,
) {
    operator fun invoke(
        category: Int,
        type: String,
        token: String,
    ): Flow<GetQuestionsByCategoryState> = callbackFlow {
        questionRepository.getQuestionsByCategory(category, type, token).collect {
            when (it) {
                is Resource.Success -> {
                    if (it.data.responseCode == 3) {
                        trySend(GetQuestionsByCategoryState.TokenEmpty)
                    } else {
                        if (it.data.questions.isNullOrEmpty().not()) {
                            trySend(GetQuestionsByCategoryState.Success(it.data.questions!!.toQuestionUIList()))
                        } else {
                            trySend(GetQuestionsByCategoryState.Error("Empty question list!"))
                        }
                    }
                }
                is Resource.Error -> {
                    trySend(GetQuestionsByCategoryState.Error(it.message))
                }
            }
        }

        awaitClose { channel.close() }
    }

    sealed class GetQuestionsByCategoryState {
        class Success(val questionList: List<QuestionUI>) : GetQuestionsByCategoryState()
        object TokenEmpty : GetQuestionsByCategoryState()
        class Error(val errorMessage: String) : GetQuestionsByCategoryState()
    }
}