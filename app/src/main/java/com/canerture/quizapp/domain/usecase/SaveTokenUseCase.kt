package com.canerture.quizapp.domain.usecase

import com.canerture.quizapp.domain.repository.QuestionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.callbackFlow

class SaveTokenUseCase @Inject constructor(
    private val questionRepository: QuestionRepository,
) {
    operator fun invoke(token: String) = callbackFlow {
        questionRepository.saveToken(token).collect {
            if (it) trySend(SaveTokenState.Success)
            else trySend(SaveTokenState.Error)
        }
    }

    sealed class SaveTokenState {
        object Success : SaveTokenState()
        object Error : SaveTokenState()
    }
}