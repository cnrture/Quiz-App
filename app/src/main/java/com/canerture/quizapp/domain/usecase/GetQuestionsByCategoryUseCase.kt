package com.canerture.quizapp.domain.usecase

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.domain.model.question.QuestionUI
import com.canerture.quizapp.domain.repository.QuestionRepository
import com.canerture.quizapp.mapper.toQuestionListUI
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetQuestionsByCategoryUseCase @Inject constructor(
    private val questionRepository: QuestionRepository,
) {
    operator fun invoke(
        category: Int,
        type: String,
        token: String,
    ): Flow<GetQuestionsByCategoryState> = flow {
        questionRepository.getQuestionsByCategory(category, type, token).collect {
            when (it) {
                is Resource.Success -> {
                    if (it.data.responseCode == TOKEN_NOT_FOUND) {
                        emit(GetQuestionsByCategoryState.EmptyToken)
                    } else {
                        if (it.data.questions.isNullOrEmpty().not()) {
                            emit(GetQuestionsByCategoryState.Success(it.data.questions!!.toQuestionListUI()))
                        } else {
                            emit(GetQuestionsByCategoryState.EmptyList)
                        }
                    }
                }

                is Resource.Error -> emit(GetQuestionsByCategoryState.Error(it.message))
            }
        }
    }

    sealed class GetQuestionsByCategoryState {
        data class Success(val questionList: List<QuestionUI>) : GetQuestionsByCategoryState()
        data class Error(val errorMessage: String) : GetQuestionsByCategoryState()
        object EmptyList : GetQuestionsByCategoryState()
        object EmptyToken : GetQuestionsByCategoryState()
    }

    companion object {
        const val TOKEN_NOT_FOUND = 3
    }
}