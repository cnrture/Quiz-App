package com.canerture.quizapp.domain.usecase

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.data.model.token.Token
import com.canerture.quizapp.domain.repository.QuestionRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GetSessionTokenUseCase @Inject constructor(
    private val questionRepository: QuestionRepository,
) {
    operator fun invoke(): Flow<Resource<Token>> = callbackFlow {
        questionRepository.getSessionToken().collect {
            when (it) {
                is Resource.Success -> {
                    it.data.token?.let { token ->
                        questionRepository.saveToken(token)
                        trySend(Resource.Success(it.data))
                    } ?: kotlin.run {
                        trySend(Resource.Error("Something went wrong!"))
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