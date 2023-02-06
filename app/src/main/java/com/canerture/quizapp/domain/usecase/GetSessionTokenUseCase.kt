package com.canerture.quizapp.domain.usecase

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.domain.repository.QuestionRepository
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GetSessionTokenUseCase @Inject constructor(
    private val questionRepository: QuestionRepository,
) {
    operator fun invoke(): Flow<GetSessionTokenState> = callbackFlow {
        questionRepository.getSessionToken().collect {
            when (it) {
                is Resource.Success -> {
                    it.data.token?.let { token ->
                        trySend(GetSessionTokenState.Success(token))
                    } ?: kotlin.run {
                        trySend(GetSessionTokenState.EmptyToken)
                    }
                }

                is Resource.Error -> {
                    trySend(GetSessionTokenState.Error(it.message))
                }
            }
        }

        awaitClose { channel.close() }
    }

    sealed class GetSessionTokenState {
        data class Success(val token: String) : GetSessionTokenState()
        object EmptyToken : GetSessionTokenState()
        data class Error(val errorMessage: String) : GetSessionTokenState()
    }
}