package com.canerture.quizapp.domain.usecase

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ResetSessionTokenUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    operator fun invoke(token: String): Flow<Resource<String>> = callbackFlow {
        questionRepository.resetSessionToken(token).collect {
            when (it) {
                is Resource.Success -> {
                    it.data.token?.let { token ->
                        questionRepository.saveToken(token)
                        trySend(Resource.Success(token))
                    } ?: kotlin.run {
                        trySend(Resource.Error("Something went wrong!"))
                    }
                }
                is Resource.Error -> {
                    trySend(Resource.Error(it.message))
                }
            }
        }
    }
}