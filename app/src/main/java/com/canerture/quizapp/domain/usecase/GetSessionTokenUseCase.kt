package com.canerture.quizapp.domain.usecase

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.domain.repository.QuestionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetSessionTokenUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    operator fun invoke(): Flow<GetSessionTokenState> = flow {
        questionRepository.getSessionToken().collect {
            when (it) {
                is Resource.Success -> {
                    it.data.token?.let { token ->
                        emit(GetSessionTokenState.Success(token))
                    } ?: kotlin.run {
                        emit(GetSessionTokenState.EmptyToken)
                    }
                }

                is Resource.Error -> {
                    emit(GetSessionTokenState.Error(it.message))
                }
            }
        }
    }

    sealed class GetSessionTokenState {
        data class Success(val token: String) : GetSessionTokenState()
        object EmptyToken : GetSessionTokenState()
        data class Error(val errorMessage: String) : GetSessionTokenState()
    }
}