package com.canerture.quizapp.domain.usecase

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.domain.repository.QuestionRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GetTokenFromDataStoreUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    operator fun invoke() = callbackFlow {
        questionRepository.getTokenFromDataStore().collect {
            it?.let {
                trySend(Resource.Success(it))
            } ?: kotlin.run {
                trySend(Resource.Error("Session token is null!"))
            }
        }

        awaitClose { channel.close() }
    }
}