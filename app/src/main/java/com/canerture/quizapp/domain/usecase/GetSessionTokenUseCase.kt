package com.canerture.quizapp.domain.usecase

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.domain.repository.QuestionRepository
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class GetSessionTokenUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    operator fun invoke() = callbackFlow {
        questionRepository.getSessionToken().collect {
            when (it) {
                is Resource.Success -> {
                    it.data.token?.let { token ->
                        trySend(Resource.Success(token))
                    } ?: kotlin.run {
                        trySend(Resource.Error("Session token is null!"))
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