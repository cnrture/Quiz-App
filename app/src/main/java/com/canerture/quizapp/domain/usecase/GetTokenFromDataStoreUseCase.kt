package com.canerture.quizapp.domain.usecase

import com.canerture.quizapp.domain.repository.QuestionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GetTokenFromDataStoreUseCase @Inject constructor(
    private val questionRepository: QuestionRepository,
) {
    operator fun invoke(): Flow<GetTokenFromDataStoreState> = callbackFlow {
        questionRepository.getTokenFromDataStore().collect {
            it?.let {
                trySend(GetTokenFromDataStoreState.Success(it))
            } ?: kotlin.run {
                trySend(GetTokenFromDataStoreState.EmptyToken)
            }
        }
    }

    sealed class GetTokenFromDataStoreState {
        data class Success(val token: String) : GetTokenFromDataStoreState()
        object EmptyToken : GetTokenFromDataStoreState()
    }
}